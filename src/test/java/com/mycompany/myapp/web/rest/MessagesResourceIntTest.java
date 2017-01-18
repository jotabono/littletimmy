package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.repository.MessagesRepository;
import com.mycompany.myapp.repository.search.MessagesSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MessagesResource REST controller.
 *
 * @see MessagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class MessagesResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    private static final ZonedDateTime DEFAULT_SEND_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_SEND_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SEND_DATE_STR = dateTimeFormatter.format(DEFAULT_SEND_DATE);

    @Inject
    private MessagesRepository messagesRepository;

    @Inject
    private MessagesSearchRepository messagesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMessagesMockMvc;

    private Messages messages;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessagesResource messagesResource = new MessagesResource();
        ReflectionTestUtils.setField(messagesResource, "messagesSearchRepository", messagesSearchRepository);
        ReflectionTestUtils.setField(messagesResource, "messagesRepository", messagesRepository);
        this.restMessagesMockMvc = MockMvcBuilders.standaloneSetup(messagesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Messages createEntity(EntityManager em) {
        Messages messages = new Messages()
                .text(DEFAULT_TEXT)
                .sendDate(DEFAULT_SEND_DATE);
        return messages;
    }

    @Before
    public void initTest() {
        messagesSearchRepository.deleteAll();
        messages = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessages() throws Exception {
        int databaseSizeBeforeCreate = messagesRepository.findAll().size();

        // Create the Messages

        restMessagesMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messages)))
                .andExpect(status().isCreated());

        // Validate the Messages in the database
        List<Messages> messages = messagesRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeCreate + 1);
        Messages testMessages = messages.get(messages.size() - 1);
        assertThat(testMessages.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testMessages.getSendDate()).isEqualTo(DEFAULT_SEND_DATE);

        // Validate the Messages in ElasticSearch
        Messages messagesEs = messagesSearchRepository.findOne(testMessages.getId());
        assertThat(messagesEs).isEqualToComparingFieldByField(testMessages);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messages
        restMessagesMockMvc.perform(get("/api/messages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(messages.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].sendDate").value(hasItem(DEFAULT_SEND_DATE_STR)));
    }

    @Test
    @Transactional
    public void getMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get the messages
        restMessagesMockMvc.perform(get("/api/messages/{id}", messages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(messages.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.sendDate").value(DEFAULT_SEND_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingMessages() throws Exception {
        // Get the messages
        restMessagesMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);
        messagesSearchRepository.save(messages);
        int databaseSizeBeforeUpdate = messagesRepository.findAll().size();

        // Update the messages
        Messages updatedMessages = messagesRepository.findOne(messages.getId());
        updatedMessages
                .text(UPDATED_TEXT)
                .sendDate(UPDATED_SEND_DATE);

        restMessagesMockMvc.perform(put("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMessages)))
                .andExpect(status().isOk());

        // Validate the Messages in the database
        List<Messages> messages = messagesRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeUpdate);
        Messages testMessages = messages.get(messages.size() - 1);
        assertThat(testMessages.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testMessages.getSendDate()).isEqualTo(UPDATED_SEND_DATE);

        // Validate the Messages in ElasticSearch
        Messages messagesEs = messagesSearchRepository.findOne(testMessages.getId());
        assertThat(messagesEs).isEqualToComparingFieldByField(testMessages);
    }

    @Test
    @Transactional
    public void deleteMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);
        messagesSearchRepository.save(messages);
        int databaseSizeBeforeDelete = messagesRepository.findAll().size();

        // Get the messages
        restMessagesMockMvc.perform(delete("/api/messages/{id}", messages.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean messagesExistsInEs = messagesSearchRepository.exists(messages.getId());
        assertThat(messagesExistsInEs).isFalse();

        // Validate the database is empty
        List<Messages> messages = messagesRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);
        messagesSearchRepository.save(messages);

        // Search the messages
        restMessagesMockMvc.perform(get("/api/_search/messages?query=id:" + messages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messages.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sendDate").value(hasItem(DEFAULT_SEND_DATE_STR)));
    }
}
