package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Trabajo;
import com.mycompany.myapp.repository.TrabajoRepository;
import com.mycompany.myapp.repository.search.TrabajoSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrabajoResource REST controller.
 *
 * @see TrabajoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class TrabajoResourceIntTest {

    private static final String DEFAULT_CARGO = "AAAAA";
    private static final String UPDATED_CARGO = "BBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTUALMENTE = false;
    private static final Boolean UPDATED_ACTUALMENTE = true;
    private static final String DEFAULT_DESCRIPCION_CARGO = "AAAAA";
    private static final String UPDATED_DESCRIPCION_CARGO = "BBBBB";

    private static final byte[] DEFAULT_MULTIMEDIA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MULTIMEDIA = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_MULTIMEDIA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MULTIMEDIA_CONTENT_TYPE = "image/png";

    @Inject
    private TrabajoRepository trabajoRepository;

    @Inject
    private TrabajoSearchRepository trabajoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTrabajoMockMvc;

    private Trabajo trabajo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrabajoResource trabajoResource = new TrabajoResource();
        ReflectionTestUtils.setField(trabajoResource, "trabajoSearchRepository", trabajoSearchRepository);
        ReflectionTestUtils.setField(trabajoResource, "trabajoRepository", trabajoRepository);
        this.restTrabajoMockMvc = MockMvcBuilders.standaloneSetup(trabajoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajo createEntity(EntityManager em) {
        Trabajo trabajo = new Trabajo()
                .cargo(DEFAULT_CARGO)
                .fechaInicio(DEFAULT_FECHA_INICIO)
                .fechaFin(DEFAULT_FECHA_FIN)
                .actualmente(DEFAULT_ACTUALMENTE)
                .descripcionCargo(DEFAULT_DESCRIPCION_CARGO)
                .multimedia(DEFAULT_MULTIMEDIA)
                .multimediaContentType(DEFAULT_MULTIMEDIA_CONTENT_TYPE);
        return trabajo;
    }

    @Before
    public void initTest() {
        trabajoSearchRepository.deleteAll();
        trabajo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrabajo() throws Exception {
        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();

        // Create the Trabajo

        restTrabajoMockMvc.perform(post("/api/trabajos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajo)))
                .andExpect(status().isCreated());

        // Validate the Trabajo in the database
        List<Trabajo> trabajos = trabajoRepository.findAll();
        assertThat(trabajos).hasSize(databaseSizeBeforeCreate + 1);
        Trabajo testTrabajo = trabajos.get(trabajos.size() - 1);
        assertThat(testTrabajo.getCargo()).isEqualTo(DEFAULT_CARGO);
        assertThat(testTrabajo.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testTrabajo.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testTrabajo.isActualmente()).isEqualTo(DEFAULT_ACTUALMENTE);
        assertThat(testTrabajo.getDescripcionCargo()).isEqualTo(DEFAULT_DESCRIPCION_CARGO);
        assertThat(testTrabajo.getMultimedia()).isEqualTo(DEFAULT_MULTIMEDIA);
        assertThat(testTrabajo.getMultimediaContentType()).isEqualTo(DEFAULT_MULTIMEDIA_CONTENT_TYPE);

        // Validate the Trabajo in ElasticSearch
        Trabajo trabajoEs = trabajoSearchRepository.findOne(testTrabajo.getId());
        assertThat(trabajoEs).isEqualToComparingFieldByField(testTrabajo);
    }

    @Test
    @Transactional
    public void getAllTrabajos() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get all the trabajos
        restTrabajoMockMvc.perform(get("/api/trabajos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trabajo.getId().intValue())))
                .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())))
                .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
                .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
                .andExpect(jsonPath("$.[*].actualmente").value(hasItem(DEFAULT_ACTUALMENTE.booleanValue())))
                .andExpect(jsonPath("$.[*].descripcionCargo").value(hasItem(DEFAULT_DESCRIPCION_CARGO.toString())))
                .andExpect(jsonPath("$.[*].multimediaContentType").value(hasItem(DEFAULT_MULTIMEDIA_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].multimedia").value(hasItem(Base64Utils.encodeToString(DEFAULT_MULTIMEDIA))));
    }

    @Test
    @Transactional
    public void getTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get the trabajo
        restTrabajoMockMvc.perform(get("/api/trabajos/{id}", trabajo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trabajo.getId().intValue()))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.actualmente").value(DEFAULT_ACTUALMENTE.booleanValue()))
            .andExpect(jsonPath("$.descripcionCargo").value(DEFAULT_DESCRIPCION_CARGO.toString()))
            .andExpect(jsonPath("$.multimediaContentType").value(DEFAULT_MULTIMEDIA_CONTENT_TYPE))
            .andExpect(jsonPath("$.multimedia").value(Base64Utils.encodeToString(DEFAULT_MULTIMEDIA)));
    }

    @Test
    @Transactional
    public void getNonExistingTrabajo() throws Exception {
        // Get the trabajo
        restTrabajoMockMvc.perform(get("/api/trabajos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);
        trabajoSearchRepository.save(trabajo);
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo
        Trabajo updatedTrabajo = trabajoRepository.findOne(trabajo.getId());
        updatedTrabajo
                .cargo(UPDATED_CARGO)
                .fechaInicio(UPDATED_FECHA_INICIO)
                .fechaFin(UPDATED_FECHA_FIN)
                .actualmente(UPDATED_ACTUALMENTE)
                .descripcionCargo(UPDATED_DESCRIPCION_CARGO)
                .multimedia(UPDATED_MULTIMEDIA)
                .multimediaContentType(UPDATED_MULTIMEDIA_CONTENT_TYPE);

        restTrabajoMockMvc.perform(put("/api/trabajos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTrabajo)))
                .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajos = trabajoRepository.findAll();
        assertThat(trabajos).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajos.get(trabajos.size() - 1);
        assertThat(testTrabajo.getCargo()).isEqualTo(UPDATED_CARGO);
        assertThat(testTrabajo.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTrabajo.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testTrabajo.isActualmente()).isEqualTo(UPDATED_ACTUALMENTE);
        assertThat(testTrabajo.getDescripcionCargo()).isEqualTo(UPDATED_DESCRIPCION_CARGO);
        assertThat(testTrabajo.getMultimedia()).isEqualTo(UPDATED_MULTIMEDIA);
        assertThat(testTrabajo.getMultimediaContentType()).isEqualTo(UPDATED_MULTIMEDIA_CONTENT_TYPE);

        // Validate the Trabajo in ElasticSearch
        Trabajo trabajoEs = trabajoSearchRepository.findOne(testTrabajo.getId());
        assertThat(trabajoEs).isEqualToComparingFieldByField(testTrabajo);
    }

    @Test
    @Transactional
    public void deleteTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);
        trabajoSearchRepository.save(trabajo);
        int databaseSizeBeforeDelete = trabajoRepository.findAll().size();

        // Get the trabajo
        restTrabajoMockMvc.perform(delete("/api/trabajos/{id}", trabajo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean trabajoExistsInEs = trabajoSearchRepository.exists(trabajo.getId());
        assertThat(trabajoExistsInEs).isFalse();

        // Validate the database is empty
        List<Trabajo> trabajos = trabajoRepository.findAll();
        assertThat(trabajos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);
        trabajoSearchRepository.save(trabajo);

        // Search the trabajo
        restTrabajoMockMvc.perform(get("/api/_search/trabajos?query=id:" + trabajo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajo.getId().intValue())))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].actualmente").value(hasItem(DEFAULT_ACTUALMENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].descripcionCargo").value(hasItem(DEFAULT_DESCRIPCION_CARGO.toString())))
            .andExpect(jsonPath("$.[*].multimediaContentType").value(hasItem(DEFAULT_MULTIMEDIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].multimedia").value(hasItem(Base64Utils.encodeToString(DEFAULT_MULTIMEDIA))));
    }
}
