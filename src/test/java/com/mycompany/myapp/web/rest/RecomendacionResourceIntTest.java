package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Recomendacion;
import com.mycompany.myapp.repository.RecomendacionRepository;
import com.mycompany.myapp.repository.search.RecomendacionSearchRepository;

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
 * Test class for the RecomendacionResource REST controller.
 *
 * @see RecomendacionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class RecomendacionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TEXTO = "AAAAA";
    private static final String UPDATED_TEXTO = "BBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_ENVIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_ENVIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_ENVIO_STR = dateTimeFormatter.format(DEFAULT_FECHA_ENVIO);

    private static final ZonedDateTime DEFAULT_FECHA_RESOLUCION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_RESOLUCION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_RESOLUCION_STR = dateTimeFormatter.format(DEFAULT_FECHA_RESOLUCION);

    private static final Boolean DEFAULT_ACEPTADA = false;
    private static final Boolean UPDATED_ACEPTADA = true;

    @Inject
    private RecomendacionRepository recomendacionRepository;

    @Inject
    private RecomendacionSearchRepository recomendacionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRecomendacionMockMvc;

    private Recomendacion recomendacion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecomendacionResource recomendacionResource = new RecomendacionResource();
        ReflectionTestUtils.setField(recomendacionResource, "recomendacionSearchRepository", recomendacionSearchRepository);
        ReflectionTestUtils.setField(recomendacionResource, "recomendacionRepository", recomendacionRepository);
        this.restRecomendacionMockMvc = MockMvcBuilders.standaloneSetup(recomendacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recomendacion createEntity(EntityManager em) {
        Recomendacion recomendacion = new Recomendacion()
                .texto(DEFAULT_TEXTO)
                .fechaEnvio(DEFAULT_FECHA_ENVIO)
                .fechaResolucion(DEFAULT_FECHA_RESOLUCION)
                .aceptada(DEFAULT_ACEPTADA);
        return recomendacion;
    }

    @Before
    public void initTest() {
        recomendacionSearchRepository.deleteAll();
        recomendacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecomendacion() throws Exception {
        int databaseSizeBeforeCreate = recomendacionRepository.findAll().size();

        // Create the Recomendacion

        restRecomendacionMockMvc.perform(post("/api/recomendacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recomendacion)))
                .andExpect(status().isCreated());

        // Validate the Recomendacion in the database
        List<Recomendacion> recomendacions = recomendacionRepository.findAll();
        assertThat(recomendacions).hasSize(databaseSizeBeforeCreate + 1);
        Recomendacion testRecomendacion = recomendacions.get(recomendacions.size() - 1);
        assertThat(testRecomendacion.getTexto()).isEqualTo(DEFAULT_TEXTO);
        assertThat(testRecomendacion.getFechaEnvio()).isEqualTo(DEFAULT_FECHA_ENVIO);
        assertThat(testRecomendacion.getFechaResolucion()).isEqualTo(DEFAULT_FECHA_RESOLUCION);
        assertThat(testRecomendacion.isAceptada()).isEqualTo(DEFAULT_ACEPTADA);

        // Validate the Recomendacion in ElasticSearch
        Recomendacion recomendacionEs = recomendacionSearchRepository.findOne(testRecomendacion.getId());
        assertThat(recomendacionEs).isEqualToComparingFieldByField(testRecomendacion);
    }

    @Test
    @Transactional
    public void getAllRecomendacions() throws Exception {
        // Initialize the database
        recomendacionRepository.saveAndFlush(recomendacion);

        // Get all the recomendacions
        restRecomendacionMockMvc.perform(get("/api/recomendacions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recomendacion.getId().intValue())))
                .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO.toString())))
                .andExpect(jsonPath("$.[*].fechaEnvio").value(hasItem(DEFAULT_FECHA_ENVIO_STR)))
                .andExpect(jsonPath("$.[*].fechaResolucion").value(hasItem(DEFAULT_FECHA_RESOLUCION_STR)))
                .andExpect(jsonPath("$.[*].aceptada").value(hasItem(DEFAULT_ACEPTADA.booleanValue())));
    }

    @Test
    @Transactional
    public void getRecomendacion() throws Exception {
        // Initialize the database
        recomendacionRepository.saveAndFlush(recomendacion);

        // Get the recomendacion
        restRecomendacionMockMvc.perform(get("/api/recomendacions/{id}", recomendacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recomendacion.getId().intValue()))
            .andExpect(jsonPath("$.texto").value(DEFAULT_TEXTO.toString()))
            .andExpect(jsonPath("$.fechaEnvio").value(DEFAULT_FECHA_ENVIO_STR))
            .andExpect(jsonPath("$.fechaResolucion").value(DEFAULT_FECHA_RESOLUCION_STR))
            .andExpect(jsonPath("$.aceptada").value(DEFAULT_ACEPTADA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecomendacion() throws Exception {
        // Get the recomendacion
        restRecomendacionMockMvc.perform(get("/api/recomendacions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecomendacion() throws Exception {
        // Initialize the database
        recomendacionRepository.saveAndFlush(recomendacion);
        recomendacionSearchRepository.save(recomendacion);
        int databaseSizeBeforeUpdate = recomendacionRepository.findAll().size();

        // Update the recomendacion
        Recomendacion updatedRecomendacion = recomendacionRepository.findOne(recomendacion.getId());
        updatedRecomendacion
                .texto(UPDATED_TEXTO)
                .fechaEnvio(UPDATED_FECHA_ENVIO)
                .fechaResolucion(UPDATED_FECHA_RESOLUCION)
                .aceptada(UPDATED_ACEPTADA);

        restRecomendacionMockMvc.perform(put("/api/recomendacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRecomendacion)))
                .andExpect(status().isOk());

        // Validate the Recomendacion in the database
        List<Recomendacion> recomendacions = recomendacionRepository.findAll();
        assertThat(recomendacions).hasSize(databaseSizeBeforeUpdate);
        Recomendacion testRecomendacion = recomendacions.get(recomendacions.size() - 1);
        assertThat(testRecomendacion.getTexto()).isEqualTo(UPDATED_TEXTO);
        assertThat(testRecomendacion.getFechaEnvio()).isEqualTo(UPDATED_FECHA_ENVIO);
        assertThat(testRecomendacion.getFechaResolucion()).isEqualTo(UPDATED_FECHA_RESOLUCION);
        assertThat(testRecomendacion.isAceptada()).isEqualTo(UPDATED_ACEPTADA);

        // Validate the Recomendacion in ElasticSearch
        Recomendacion recomendacionEs = recomendacionSearchRepository.findOne(testRecomendacion.getId());
        assertThat(recomendacionEs).isEqualToComparingFieldByField(testRecomendacion);
    }

    @Test
    @Transactional
    public void deleteRecomendacion() throws Exception {
        // Initialize the database
        recomendacionRepository.saveAndFlush(recomendacion);
        recomendacionSearchRepository.save(recomendacion);
        int databaseSizeBeforeDelete = recomendacionRepository.findAll().size();

        // Get the recomendacion
        restRecomendacionMockMvc.perform(delete("/api/recomendacions/{id}", recomendacion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean recomendacionExistsInEs = recomendacionSearchRepository.exists(recomendacion.getId());
        assertThat(recomendacionExistsInEs).isFalse();

        // Validate the database is empty
        List<Recomendacion> recomendacions = recomendacionRepository.findAll();
        assertThat(recomendacions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRecomendacion() throws Exception {
        // Initialize the database
        recomendacionRepository.saveAndFlush(recomendacion);
        recomendacionSearchRepository.save(recomendacion);

        // Search the recomendacion
        restRecomendacionMockMvc.perform(get("/api/_search/recomendacions?query=id:" + recomendacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recomendacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO.toString())))
            .andExpect(jsonPath("$.[*].fechaEnvio").value(hasItem(DEFAULT_FECHA_ENVIO_STR)))
            .andExpect(jsonPath("$.[*].fechaResolucion").value(hasItem(DEFAULT_FECHA_RESOLUCION_STR)))
            .andExpect(jsonPath("$.[*].aceptada").value(hasItem(DEFAULT_ACEPTADA.booleanValue())));
    }
}
