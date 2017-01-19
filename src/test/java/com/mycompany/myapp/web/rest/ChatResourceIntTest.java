package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Chat;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ChatRepository;
import com.mycompany.myapp.repository.search.ChatSearchRepository;

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
 * Test class for the ChatResource REST controller.
 *
 * @see ChatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class ChatResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    @Inject
    private ChatRepository chatRepository;

    @Inject
    private ChatSearchRepository chatSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restChatMockMvc;

    private Chat chat;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChatResource chatResource = new ChatResource();
        ReflectionTestUtils.setField(chatResource, "chatSearchRepository", chatSearchRepository);
        ReflectionTestUtils.setField(chatResource, "chatRepository", chatRepository);
        this.restChatMockMvc = MockMvcBuilders.standaloneSetup(chatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chat createEntity(EntityManager em) {
        Chat chat = new Chat()
                .name(DEFAULT_NAME)
                .creationDate(DEFAULT_CREATION_DATE);
        // Add required entity
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        chat.setOwner(owner);
        // Add required entity
        User users = UserResourceIntTest.createEntity(em);
        em.persist(users);
        em.flush();
        chat.getUsers().add(users);
        return chat;
    }

    @Before
    public void initTest() {
        chatSearchRepository.deleteAll();
        chat = createEntity(em);
    }

    @Test
    @Transactional
    public void createChat() throws Exception {
        int databaseSizeBeforeCreate = chatRepository.findAll().size();

        // Create the Chat

        restChatMockMvc.perform(post("/api/chats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chat)))
                .andExpect(status().isCreated());

        // Validate the Chat in the database
        List<Chat> chats = chatRepository.findAll();
        assertThat(chats).hasSize(databaseSizeBeforeCreate + 1);
        Chat testChat = chats.get(chats.size() - 1);
        assertThat(testChat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChat.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);

        // Validate the Chat in ElasticSearch
        Chat chatEs = chatSearchRepository.findOne(testChat.getId());
        assertThat(chatEs).isEqualToComparingFieldByField(testChat);
    }

    @Test
    @Transactional
    public void getAllChats() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        // Get all the chats
        restChatMockMvc.perform(get("/api/chats?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chat.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)));
    }

    @Test
    @Transactional
    public void getChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        // Get the chat
        restChatMockMvc.perform(get("/api/chats/{id}", chat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingChat() throws Exception {
        // Get the chat
        restChatMockMvc.perform(get("/api/chats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);
        chatSearchRepository.save(chat);
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();

        // Update the chat
        Chat updatedChat = chatRepository.findOne(chat.getId());
        updatedChat
                .name(UPDATED_NAME)
                .creationDate(UPDATED_CREATION_DATE);

        restChatMockMvc.perform(put("/api/chats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChat)))
                .andExpect(status().isOk());

        // Validate the Chat in the database
        List<Chat> chats = chatRepository.findAll();
        assertThat(chats).hasSize(databaseSizeBeforeUpdate);
        Chat testChat = chats.get(chats.size() - 1);
        assertThat(testChat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChat.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);

        // Validate the Chat in ElasticSearch
        Chat chatEs = chatSearchRepository.findOne(testChat.getId());
        assertThat(chatEs).isEqualToComparingFieldByField(testChat);
    }

    @Test
    @Transactional
    public void deleteChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);
        chatSearchRepository.save(chat);
        int databaseSizeBeforeDelete = chatRepository.findAll().size();

        // Get the chat
        restChatMockMvc.perform(delete("/api/chats/{id}", chat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean chatExistsInEs = chatSearchRepository.exists(chat.getId());
        assertThat(chatExistsInEs).isFalse();

        // Validate the database is empty
        List<Chat> chats = chatRepository.findAll();
        assertThat(chats).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);
        chatSearchRepository.save(chat);

        // Search the chat
        restChatMockMvc.perform(get("/api/_search/chats?query=id:" + chat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)));
    }
}
