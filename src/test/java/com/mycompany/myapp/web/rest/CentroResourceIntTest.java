package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Centro;
import com.mycompany.myapp.repository.CentroRepository;
import com.mycompany.myapp.repository.search.CentroSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CentroResource REST controller.
 *
 * @see CentroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class CentroResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final Integer DEFAULT_NUM_EMPLEADOS = 1;
    private static final Integer UPDATED_NUM_EMPLEADOS = 2;

    private static final LocalDate DEFAULT_FECHA_FUNDACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FUNDACION = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_UBICACION = "AAAAA";
    private static final String UPDATED_UBICACION = "BBBBB";
    private static final String DEFAULT_LATITUD = "AAAAA";
    private static final String UPDATED_LATITUD = "BBBBB";
    private static final String DEFAULT_LONGITUD = "AAAAA";
    private static final String UPDATED_LONGITUD = "BBBBB";

    @Inject
    private CentroRepository centroRepository;

    @Inject
    private CentroSearchRepository centroSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCentroMockMvc;

    private Centro centro;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CentroResource centroResource = new CentroResource();
        ReflectionTestUtils.setField(centroResource, "centroSearchRepository", centroSearchRepository);
        ReflectionTestUtils.setField(centroResource, "centroRepository", centroRepository);
        this.restCentroMockMvc = MockMvcBuilders.standaloneSetup(centroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centro createEntity(EntityManager em) {
        Centro centro = new Centro()
                .nombre(DEFAULT_NOMBRE)
                .numEmpleados(DEFAULT_NUM_EMPLEADOS)
                .fechaFundacion(DEFAULT_FECHA_FUNDACION)
                .ubicacion(DEFAULT_UBICACION)
                .latitud(DEFAULT_LATITUD)
                .longitud(DEFAULT_LONGITUD);
        return centro;
    }

    @Before
    public void initTest() {
        centroSearchRepository.deleteAll();
        centro = createEntity(em);
    }

    @Test
    @Transactional
    public void createCentro() throws Exception {
        int databaseSizeBeforeCreate = centroRepository.findAll().size();

        // Create the Centro

        restCentroMockMvc.perform(post("/api/centros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(centro)))
                .andExpect(status().isCreated());

        // Validate the Centro in the database
        List<Centro> centros = centroRepository.findAll();
        assertThat(centros).hasSize(databaseSizeBeforeCreate + 1);
        Centro testCentro = centros.get(centros.size() - 1);
        assertThat(testCentro.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCentro.getNumEmpleados()).isEqualTo(DEFAULT_NUM_EMPLEADOS);
        assertThat(testCentro.getFechaFundacion()).isEqualTo(DEFAULT_FECHA_FUNDACION);
        assertThat(testCentro.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
        assertThat(testCentro.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testCentro.getLongitud()).isEqualTo(DEFAULT_LONGITUD);

        // Validate the Centro in ElasticSearch
        Centro centroEs = centroSearchRepository.findOne(testCentro.getId());
        assertThat(centroEs).isEqualToComparingFieldByField(testCentro);
    }

    @Test
    @Transactional
    public void getAllCentros() throws Exception {
        // Initialize the database
        centroRepository.saveAndFlush(centro);

        // Get all the centros
        restCentroMockMvc.perform(get("/api/centros?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(centro.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].numEmpleados").value(hasItem(DEFAULT_NUM_EMPLEADOS)))
                .andExpect(jsonPath("$.[*].fechaFundacion").value(hasItem(DEFAULT_FECHA_FUNDACION.toString())))
                .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION.toString())))
                .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.toString())))
                .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.toString())));
    }

    @Test
    @Transactional
    public void getCentro() throws Exception {
        // Initialize the database
        centroRepository.saveAndFlush(centro);

        // Get the centro
        restCentroMockMvc.perform(get("/api/centros/{id}", centro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(centro.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.numEmpleados").value(DEFAULT_NUM_EMPLEADOS))
            .andExpect(jsonPath("$.fechaFundacion").value(DEFAULT_FECHA_FUNDACION.toString()))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION.toString()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.toString()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCentro() throws Exception {
        // Get the centro
        restCentroMockMvc.perform(get("/api/centros/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCentro() throws Exception {
        // Initialize the database
        centroRepository.saveAndFlush(centro);
        centroSearchRepository.save(centro);
        int databaseSizeBeforeUpdate = centroRepository.findAll().size();

        // Update the centro
        Centro updatedCentro = centroRepository.findOne(centro.getId());
        updatedCentro
                .nombre(UPDATED_NOMBRE)
                .numEmpleados(UPDATED_NUM_EMPLEADOS)
                .fechaFundacion(UPDATED_FECHA_FUNDACION)
                .ubicacion(UPDATED_UBICACION)
                .latitud(UPDATED_LATITUD)
                .longitud(UPDATED_LONGITUD);

        restCentroMockMvc.perform(put("/api/centros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCentro)))
                .andExpect(status().isOk());

        // Validate the Centro in the database
        List<Centro> centros = centroRepository.findAll();
        assertThat(centros).hasSize(databaseSizeBeforeUpdate);
        Centro testCentro = centros.get(centros.size() - 1);
        assertThat(testCentro.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCentro.getNumEmpleados()).isEqualTo(UPDATED_NUM_EMPLEADOS);
        assertThat(testCentro.getFechaFundacion()).isEqualTo(UPDATED_FECHA_FUNDACION);
        assertThat(testCentro.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testCentro.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testCentro.getLongitud()).isEqualTo(UPDATED_LONGITUD);

        // Validate the Centro in ElasticSearch
        Centro centroEs = centroSearchRepository.findOne(testCentro.getId());
        assertThat(centroEs).isEqualToComparingFieldByField(testCentro);
    }

    @Test
    @Transactional
    public void deleteCentro() throws Exception {
        // Initialize the database
        centroRepository.saveAndFlush(centro);
        centroSearchRepository.save(centro);
        int databaseSizeBeforeDelete = centroRepository.findAll().size();

        // Get the centro
        restCentroMockMvc.perform(delete("/api/centros/{id}", centro.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean centroExistsInEs = centroSearchRepository.exists(centro.getId());
        assertThat(centroExistsInEs).isFalse();

        // Validate the database is empty
        List<Centro> centros = centroRepository.findAll();
        assertThat(centros).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCentro() throws Exception {
        // Initialize the database
        centroRepository.saveAndFlush(centro);
        centroSearchRepository.save(centro);

        // Search the centro
        restCentroMockMvc.perform(get("/api/_search/centros?query=id:" + centro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].numEmpleados").value(hasItem(DEFAULT_NUM_EMPLEADOS)))
            .andExpect(jsonPath("$.[*].fechaFundacion").value(hasItem(DEFAULT_FECHA_FUNDACION.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION.toString())))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.toString())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.toString())));
    }
}
