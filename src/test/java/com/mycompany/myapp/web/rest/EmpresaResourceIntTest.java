package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Empresa;
import com.mycompany.myapp.repository.EmpresaRepository;
import com.mycompany.myapp.repository.search.EmpresaSearchRepository;

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
 * Test class for the EmpresaResource REST controller.
 *
 * @see EmpresaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class EmpresaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final Integer DEFAULT_NUM_EMPLEADOS = 1;
    private static final Integer UPDATED_NUM_EMPLEADOS = 2;
    private static final String DEFAULT_UBICACION = "AAAAA";
    private static final String UPDATED_UBICACION = "BBBBB";
    private static final String DEFAULT_LATITUD = "AAAAA";
    private static final String UPDATED_LATITUD = "BBBBB";
    private static final String DEFAULT_LONGITUD = "AAAAA";
    private static final String UPDATED_LONGITUD = "BBBBB";

    private static final LocalDate DEFAULT_FECHA_FUNDACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FUNDACION = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private EmpresaRepository empresaRepository;

    @Inject
    private EmpresaSearchRepository empresaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEmpresaMockMvc;

    private Empresa empresa;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmpresaResource empresaResource = new EmpresaResource();
        ReflectionTestUtils.setField(empresaResource, "empresaSearchRepository", empresaSearchRepository);
        ReflectionTestUtils.setField(empresaResource, "empresaRepository", empresaRepository);
        this.restEmpresaMockMvc = MockMvcBuilders.standaloneSetup(empresaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createEntity(EntityManager em) {
        Empresa empresa = new Empresa()
                .nombre(DEFAULT_NOMBRE)
                .numEmpleados(DEFAULT_NUM_EMPLEADOS)
                .ubicacion(DEFAULT_UBICACION)
                .latitud(DEFAULT_LATITUD)
                .longitud(DEFAULT_LONGITUD)
                .fechaFundacion(DEFAULT_FECHA_FUNDACION);
        return empresa;
    }

    @Before
    public void initTest() {
        empresaSearchRepository.deleteAll();
        empresa = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmpresa() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().size();

        // Create the Empresa

        restEmpresaMockMvc.perform(post("/api/empresas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(empresa)))
                .andExpect(status().isCreated());

        // Validate the Empresa in the database
        List<Empresa> empresas = empresaRepository.findAll();
        assertThat(empresas).hasSize(databaseSizeBeforeCreate + 1);
        Empresa testEmpresa = empresas.get(empresas.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEmpresa.getNumEmpleados()).isEqualTo(DEFAULT_NUM_EMPLEADOS);
        assertThat(testEmpresa.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
        assertThat(testEmpresa.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testEmpresa.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testEmpresa.getFechaFundacion()).isEqualTo(DEFAULT_FECHA_FUNDACION);

        // Validate the Empresa in ElasticSearch
        Empresa empresaEs = empresaSearchRepository.findOne(testEmpresa.getId());
        assertThat(empresaEs).isEqualToComparingFieldByField(testEmpresa);
    }

    @Test
    @Transactional
    public void getAllEmpresas() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresas
        restEmpresaMockMvc.perform(get("/api/empresas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].numEmpleados").value(hasItem(DEFAULT_NUM_EMPLEADOS)))
                .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION.toString())))
                .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.toString())))
                .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.toString())))
                .andExpect(jsonPath("$.[*].fechaFundacion").value(hasItem(DEFAULT_FECHA_FUNDACION.toString())));
    }

    @Test
    @Transactional
    public void getEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get the empresa
        restEmpresaMockMvc.perform(get("/api/empresas/{id}", empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(empresa.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.numEmpleados").value(DEFAULT_NUM_EMPLEADOS))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION.toString()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.toString()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.toString()))
            .andExpect(jsonPath("$.fechaFundacion").value(DEFAULT_FECHA_FUNDACION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmpresa() throws Exception {
        // Get the empresa
        restEmpresaMockMvc.perform(get("/api/empresas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        empresaSearchRepository.save(empresa);
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findOne(empresa.getId());
        updatedEmpresa
                .nombre(UPDATED_NOMBRE)
                .numEmpleados(UPDATED_NUM_EMPLEADOS)
                .ubicacion(UPDATED_UBICACION)
                .latitud(UPDATED_LATITUD)
                .longitud(UPDATED_LONGITUD)
                .fechaFundacion(UPDATED_FECHA_FUNDACION);

        restEmpresaMockMvc.perform(put("/api/empresas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmpresa)))
                .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresas = empresaRepository.findAll();
        assertThat(empresas).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresas.get(empresas.size() - 1);
        assertThat(testEmpresa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpresa.getNumEmpleados()).isEqualTo(UPDATED_NUM_EMPLEADOS);
        assertThat(testEmpresa.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testEmpresa.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testEmpresa.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testEmpresa.getFechaFundacion()).isEqualTo(UPDATED_FECHA_FUNDACION);

        // Validate the Empresa in ElasticSearch
        Empresa empresaEs = empresaSearchRepository.findOne(testEmpresa.getId());
        assertThat(empresaEs).isEqualToComparingFieldByField(testEmpresa);
    }

    @Test
    @Transactional
    public void deleteEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        empresaSearchRepository.save(empresa);
        int databaseSizeBeforeDelete = empresaRepository.findAll().size();

        // Get the empresa
        restEmpresaMockMvc.perform(delete("/api/empresas/{id}", empresa.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean empresaExistsInEs = empresaSearchRepository.exists(empresa.getId());
        assertThat(empresaExistsInEs).isFalse();

        // Validate the database is empty
        List<Empresa> empresas = empresaRepository.findAll();
        assertThat(empresas).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);
        empresaSearchRepository.save(empresa);

        // Search the empresa
        restEmpresaMockMvc.perform(get("/api/_search/empresas?query=id:" + empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].numEmpleados").value(hasItem(DEFAULT_NUM_EMPLEADOS)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION.toString())))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.toString())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.toString())))
            .andExpect(jsonPath("$.[*].fechaFundacion").value(hasItem(DEFAULT_FECHA_FUNDACION.toString())));
    }
}
