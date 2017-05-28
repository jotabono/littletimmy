package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Friendship_notification;
import com.mycompany.myapp.repository.Friendship_notificationRepository;
import com.mycompany.myapp.repository.search.Friendship_notificationSearchRepository;

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
 * Test class for the Friendship_notificationResource REST controller.
 *
 * @see Friendship_notificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class Friendship_notificationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_FECHA_RECIBIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_RECIBIDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_RECIBIDA_STR = dateTimeFormatter.format(DEFAULT_FECHA_RECIBIDA);

    private static final Boolean DEFAULT_LEIDA = false;
    private static final Boolean UPDATED_LEIDA = true;

    @Inject
    private Friendship_notificationRepository friendship_notificationRepository;

    @Inject
    private Friendship_notificationSearchRepository friendship_notificationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFriendship_notificationMockMvc;

    private Friendship_notification friendship_notification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Friendship_notificationResource friendship_notificationResource = new Friendship_notificationResource();
        ReflectionTestUtils.setField(friendship_notificationResource, "friendship_notificationSearchRepository", friendship_notificationSearchRepository);
        ReflectionTestUtils.setField(friendship_notificationResource, "friendship_notificationRepository", friendship_notificationRepository);
        this.restFriendship_notificationMockMvc = MockMvcBuilders.standaloneSetup(friendship_notificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friendship_notification createEntity(EntityManager em) {
        Friendship_notification friendship_notification = new Friendship_notification()
                .fechaRecibida(DEFAULT_FECHA_RECIBIDA)
                .leida(DEFAULT_LEIDA);
        return friendship_notification;
    }

    @Before
    public void initTest() {
        friendship_notificationSearchRepository.deleteAll();
        friendship_notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createFriendship_notification() throws Exception {
        int databaseSizeBeforeCreate = friendship_notificationRepository.findAll().size();

        // Create the Friendship_notification

        restFriendship_notificationMockMvc.perform(post("/api/friendship-notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(friendship_notification)))
                .andExpect(status().isCreated());

        // Validate the Friendship_notification in the database
        List<Friendship_notification> friendship_notifications = friendship_notificationRepository.findAll();
        assertThat(friendship_notifications).hasSize(databaseSizeBeforeCreate + 1);
        Friendship_notification testFriendship_notification = friendship_notifications.get(friendship_notifications.size() - 1);
        assertThat(testFriendship_notification.getFechaRecibida()).isEqualTo(DEFAULT_FECHA_RECIBIDA);
        assertThat(testFriendship_notification.isLeida()).isEqualTo(DEFAULT_LEIDA);

        // Validate the Friendship_notification in ElasticSearch
        Friendship_notification friendship_notificationEs = friendship_notificationSearchRepository.findOne(testFriendship_notification.getId());
        assertThat(friendship_notificationEs).isEqualToComparingFieldByField(testFriendship_notification);
    }

    @Test
    @Transactional
    public void getAllFriendship_notifications() throws Exception {
        // Initialize the database
        friendship_notificationRepository.saveAndFlush(friendship_notification);

        // Get all the friendship_notifications
        restFriendship_notificationMockMvc.perform(get("/api/friendship-notifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(friendship_notification.getId().intValue())))
                .andExpect(jsonPath("$.[*].fechaRecibida").value(hasItem(DEFAULT_FECHA_RECIBIDA_STR)))
                .andExpect(jsonPath("$.[*].leida").value(hasItem(DEFAULT_LEIDA.booleanValue())));
    }

    @Test
    @Transactional
    public void getFriendship_notification() throws Exception {
        // Initialize the database
        friendship_notificationRepository.saveAndFlush(friendship_notification);

        // Get the friendship_notification
        restFriendship_notificationMockMvc.perform(get("/api/friendship-notifications/{id}", friendship_notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(friendship_notification.getId().intValue()))
            .andExpect(jsonPath("$.fechaRecibida").value(DEFAULT_FECHA_RECIBIDA_STR))
            .andExpect(jsonPath("$.leida").value(DEFAULT_LEIDA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFriendship_notification() throws Exception {
        // Get the friendship_notification
        restFriendship_notificationMockMvc.perform(get("/api/friendship-notifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriendship_notification() throws Exception {
        // Initialize the database
        friendship_notificationRepository.saveAndFlush(friendship_notification);
        friendship_notificationSearchRepository.save(friendship_notification);
        int databaseSizeBeforeUpdate = friendship_notificationRepository.findAll().size();

        // Update the friendship_notification
        Friendship_notification updatedFriendship_notification = friendship_notificationRepository.findOne(friendship_notification.getId());
        updatedFriendship_notification
                .fechaRecibida(UPDATED_FECHA_RECIBIDA)
                .leida(UPDATED_LEIDA);

        restFriendship_notificationMockMvc.perform(put("/api/friendship-notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFriendship_notification)))
                .andExpect(status().isOk());

        // Validate the Friendship_notification in the database
        List<Friendship_notification> friendship_notifications = friendship_notificationRepository.findAll();
        assertThat(friendship_notifications).hasSize(databaseSizeBeforeUpdate);
        Friendship_notification testFriendship_notification = friendship_notifications.get(friendship_notifications.size() - 1);
        assertThat(testFriendship_notification.getFechaRecibida()).isEqualTo(UPDATED_FECHA_RECIBIDA);
        assertThat(testFriendship_notification.isLeida()).isEqualTo(UPDATED_LEIDA);

        // Validate the Friendship_notification in ElasticSearch
        Friendship_notification friendship_notificationEs = friendship_notificationSearchRepository.findOne(testFriendship_notification.getId());
        assertThat(friendship_notificationEs).isEqualToComparingFieldByField(testFriendship_notification);
    }

    @Test
    @Transactional
    public void deleteFriendship_notification() throws Exception {
        // Initialize the database
        friendship_notificationRepository.saveAndFlush(friendship_notification);
        friendship_notificationSearchRepository.save(friendship_notification);
        int databaseSizeBeforeDelete = friendship_notificationRepository.findAll().size();

        // Get the friendship_notification
        restFriendship_notificationMockMvc.perform(delete("/api/friendship-notifications/{id}", friendship_notification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean friendship_notificationExistsInEs = friendship_notificationSearchRepository.exists(friendship_notification.getId());
        assertThat(friendship_notificationExistsInEs).isFalse();

        // Validate the database is empty
        List<Friendship_notification> friendship_notifications = friendship_notificationRepository.findAll();
        assertThat(friendship_notifications).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFriendship_notification() throws Exception {
        // Initialize the database
        friendship_notificationRepository.saveAndFlush(friendship_notification);
        friendship_notificationSearchRepository.save(friendship_notification);

        // Search the friendship_notification
        restFriendship_notificationMockMvc.perform(get("/api/_search/friendship-notifications?query=id:" + friendship_notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friendship_notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaRecibida").value(hasItem(DEFAULT_FECHA_RECIBIDA_STR)))
            .andExpect(jsonPath("$.[*].leida").value(hasItem(DEFAULT_LEIDA.booleanValue())));
    }
}
