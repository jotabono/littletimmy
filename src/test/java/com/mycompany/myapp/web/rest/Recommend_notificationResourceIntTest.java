package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Recommend_notification;
import com.mycompany.myapp.repository.Recommend_notificationRepository;
import com.mycompany.myapp.repository.search.Recommend_notificationSearchRepository;

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
 * Test class for the Recommend_notificationResource REST controller.
 *
 * @see Recommend_notificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class Recommend_notificationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CONTENIDO = "AAAAA";
    private static final String UPDATED_CONTENIDO = "BBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_RECIBIDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_RECIBIDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_RECIBIDA_STR = dateTimeFormatter.format(DEFAULT_FECHA_RECIBIDA);

    private static final Boolean DEFAULT_LEIDA = false;
    private static final Boolean UPDATED_LEIDA = true;

    @Inject
    private Recommend_notificationRepository recommend_notificationRepository;

    @Inject
    private Recommend_notificationSearchRepository recommend_notificationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRecommend_notificationMockMvc;

    private Recommend_notification recommend_notification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Recommend_notificationResource recommend_notificationResource = new Recommend_notificationResource();
        ReflectionTestUtils.setField(recommend_notificationResource, "recommend_notificationSearchRepository", recommend_notificationSearchRepository);
        ReflectionTestUtils.setField(recommend_notificationResource, "recommend_notificationRepository", recommend_notificationRepository);
        this.restRecommend_notificationMockMvc = MockMvcBuilders.standaloneSetup(recommend_notificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommend_notification createEntity(EntityManager em) {
        Recommend_notification recommend_notification = new Recommend_notification()
                .contenido(DEFAULT_CONTENIDO)
                .fechaRecibida(DEFAULT_FECHA_RECIBIDA)
                .leida(DEFAULT_LEIDA);
        return recommend_notification;
    }

    @Before
    public void initTest() {
        recommend_notificationSearchRepository.deleteAll();
        recommend_notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecommend_notification() throws Exception {
        int databaseSizeBeforeCreate = recommend_notificationRepository.findAll().size();

        // Create the Recommend_notification

        restRecommend_notificationMockMvc.perform(post("/api/recommend-notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recommend_notification)))
                .andExpect(status().isCreated());

        // Validate the Recommend_notification in the database
        List<Recommend_notification> recommend_notifications = recommend_notificationRepository.findAll();
        assertThat(recommend_notifications).hasSize(databaseSizeBeforeCreate + 1);
        Recommend_notification testRecommend_notification = recommend_notifications.get(recommend_notifications.size() - 1);
        assertThat(testRecommend_notification.getContenido()).isEqualTo(DEFAULT_CONTENIDO);
        assertThat(testRecommend_notification.getFechaRecibida()).isEqualTo(DEFAULT_FECHA_RECIBIDA);
        assertThat(testRecommend_notification.isLeida()).isEqualTo(DEFAULT_LEIDA);

        // Validate the Recommend_notification in ElasticSearch
        Recommend_notification recommend_notificationEs = recommend_notificationSearchRepository.findOne(testRecommend_notification.getId());
        assertThat(recommend_notificationEs).isEqualToComparingFieldByField(testRecommend_notification);
    }

    @Test
    @Transactional
    public void getAllRecommend_notifications() throws Exception {
        // Initialize the database
        recommend_notificationRepository.saveAndFlush(recommend_notification);

        // Get all the recommend_notifications
        restRecommend_notificationMockMvc.perform(get("/api/recommend-notifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recommend_notification.getId().intValue())))
                .andExpect(jsonPath("$.[*].contenido").value(hasItem(DEFAULT_CONTENIDO.toString())))
                .andExpect(jsonPath("$.[*].fechaRecibida").value(hasItem(DEFAULT_FECHA_RECIBIDA_STR)))
                .andExpect(jsonPath("$.[*].leida").value(hasItem(DEFAULT_LEIDA.booleanValue())));
    }

    @Test
    @Transactional
    public void getRecommend_notification() throws Exception {
        // Initialize the database
        recommend_notificationRepository.saveAndFlush(recommend_notification);

        // Get the recommend_notification
        restRecommend_notificationMockMvc.perform(get("/api/recommend-notifications/{id}", recommend_notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recommend_notification.getId().intValue()))
            .andExpect(jsonPath("$.contenido").value(DEFAULT_CONTENIDO.toString()))
            .andExpect(jsonPath("$.fechaRecibida").value(DEFAULT_FECHA_RECIBIDA_STR))
            .andExpect(jsonPath("$.leida").value(DEFAULT_LEIDA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecommend_notification() throws Exception {
        // Get the recommend_notification
        restRecommend_notificationMockMvc.perform(get("/api/recommend-notifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecommend_notification() throws Exception {
        // Initialize the database
        recommend_notificationRepository.saveAndFlush(recommend_notification);
        recommend_notificationSearchRepository.save(recommend_notification);
        int databaseSizeBeforeUpdate = recommend_notificationRepository.findAll().size();

        // Update the recommend_notification
        Recommend_notification updatedRecommend_notification = recommend_notificationRepository.findOne(recommend_notification.getId());
        updatedRecommend_notification
                .contenido(UPDATED_CONTENIDO)
                .fechaRecibida(UPDATED_FECHA_RECIBIDA)
                .leida(UPDATED_LEIDA);

        restRecommend_notificationMockMvc.perform(put("/api/recommend-notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRecommend_notification)))
                .andExpect(status().isOk());

        // Validate the Recommend_notification in the database
        List<Recommend_notification> recommend_notifications = recommend_notificationRepository.findAll();
        assertThat(recommend_notifications).hasSize(databaseSizeBeforeUpdate);
        Recommend_notification testRecommend_notification = recommend_notifications.get(recommend_notifications.size() - 1);
        assertThat(testRecommend_notification.getContenido()).isEqualTo(UPDATED_CONTENIDO);
        assertThat(testRecommend_notification.getFechaRecibida()).isEqualTo(UPDATED_FECHA_RECIBIDA);
        assertThat(testRecommend_notification.isLeida()).isEqualTo(UPDATED_LEIDA);

        // Validate the Recommend_notification in ElasticSearch
        Recommend_notification recommend_notificationEs = recommend_notificationSearchRepository.findOne(testRecommend_notification.getId());
        assertThat(recommend_notificationEs).isEqualToComparingFieldByField(testRecommend_notification);
    }

    @Test
    @Transactional
    public void deleteRecommend_notification() throws Exception {
        // Initialize the database
        recommend_notificationRepository.saveAndFlush(recommend_notification);
        recommend_notificationSearchRepository.save(recommend_notification);
        int databaseSizeBeforeDelete = recommend_notificationRepository.findAll().size();

        // Get the recommend_notification
        restRecommend_notificationMockMvc.perform(delete("/api/recommend-notifications/{id}", recommend_notification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean recommend_notificationExistsInEs = recommend_notificationSearchRepository.exists(recommend_notification.getId());
        assertThat(recommend_notificationExistsInEs).isFalse();

        // Validate the database is empty
        List<Recommend_notification> recommend_notifications = recommend_notificationRepository.findAll();
        assertThat(recommend_notifications).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRecommend_notification() throws Exception {
        // Initialize the database
        recommend_notificationRepository.saveAndFlush(recommend_notification);
        recommend_notificationSearchRepository.save(recommend_notification);

        // Search the recommend_notification
        restRecommend_notificationMockMvc.perform(get("/api/_search/recommend-notifications?query=id:" + recommend_notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recommend_notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenido").value(hasItem(DEFAULT_CONTENIDO.toString())))
            .andExpect(jsonPath("$.[*].fechaRecibida").value(hasItem(DEFAULT_FECHA_RECIBIDA_STR)))
            .andExpect(jsonPath("$.[*].leida").value(hasItem(DEFAULT_LEIDA.booleanValue())));
    }
}
