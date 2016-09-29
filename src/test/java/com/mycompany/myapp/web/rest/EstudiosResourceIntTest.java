package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Estudios;
import com.mycompany.myapp.repository.EstudiosRepository;
import com.mycompany.myapp.repository.search.EstudiosSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EstudiosResource REST controller.
 *
 * @see EstudiosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class EstudiosResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CENTRO = "AAAAA";
    private static final String UPDATED_CENTRO = "BBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_INICIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_INICIO_STR = dateTimeFormatter.format(DEFAULT_FECHA_INICIO);

    private static final ZonedDateTime DEFAULT_FECHA_FINAL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_FINAL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_FINAL_STR = dateTimeFormatter.format(DEFAULT_FECHA_FINAL);

    private static final Boolean DEFAULT_ACTUALMENTE = false;
    private static final Boolean UPDATED_ACTUALMENTE = true;
    private static final String DEFAULT_CURSO = "AAAAA";
    private static final String UPDATED_CURSO = "BBBBB";

    private static final Float DEFAULT_NOTA = 1F;
    private static final Float UPDATED_NOTA = 2F;
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    private static final byte[] DEFAULT_ARCHIVOS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ARCHIVOS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ARCHIVOS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ARCHIVOS_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_LINK = "AAAAA";
    private static final String UPDATED_LINK = "BBBBB";

    @Inject
    private EstudiosRepository estudiosRepository;

    @Inject
    private EstudiosSearchRepository estudiosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEstudiosMockMvc;

    private Estudios estudios;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstudiosResource estudiosResource = new EstudiosResource();
        ReflectionTestUtils.setField(estudiosResource, "estudiosSearchRepository", estudiosSearchRepository);
        ReflectionTestUtils.setField(estudiosResource, "estudiosRepository", estudiosRepository);
        this.restEstudiosMockMvc = MockMvcBuilders.standaloneSetup(estudiosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estudios createEntity(EntityManager em) {
        Estudios estudios = new Estudios()
                .centro(DEFAULT_CENTRO)
                .fechaInicio(DEFAULT_FECHA_INICIO)
                .fechaFinal(DEFAULT_FECHA_FINAL)
                .actualmente(DEFAULT_ACTUALMENTE)
                .curso(DEFAULT_CURSO)
                .nota(DEFAULT_NOTA)
                .descripcion(DEFAULT_DESCRIPCION)
                .archivos(DEFAULT_ARCHIVOS)
                .archivosContentType(DEFAULT_ARCHIVOS_CONTENT_TYPE)
                .link(DEFAULT_LINK);
        return estudios;
    }

    @Before
    public void initTest() {
        estudiosSearchRepository.deleteAll();
        estudios = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstudios() throws Exception {
        int databaseSizeBeforeCreate = estudiosRepository.findAll().size();

        // Create the Estudios

        restEstudiosMockMvc.perform(post("/api/estudios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estudios)))
                .andExpect(status().isCreated());

        // Validate the Estudios in the database
        List<Estudios> estudios = estudiosRepository.findAll();
        assertThat(estudios).hasSize(databaseSizeBeforeCreate + 1);
        Estudios testEstudios = estudios.get(estudios.size() - 1);
        assertThat(testEstudios.getCentro()).isEqualTo(DEFAULT_CENTRO);
        assertThat(testEstudios.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testEstudios.getFechaFinal()).isEqualTo(DEFAULT_FECHA_FINAL);
        assertThat(testEstudios.isActualmente()).isEqualTo(DEFAULT_ACTUALMENTE);
        assertThat(testEstudios.getCurso()).isEqualTo(DEFAULT_CURSO);
        assertThat(testEstudios.getNota()).isEqualTo(DEFAULT_NOTA);
        assertThat(testEstudios.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testEstudios.getArchivos()).isEqualTo(DEFAULT_ARCHIVOS);
        assertThat(testEstudios.getArchivosContentType()).isEqualTo(DEFAULT_ARCHIVOS_CONTENT_TYPE);
        assertThat(testEstudios.getLink()).isEqualTo(DEFAULT_LINK);

        // Validate the Estudios in ElasticSearch
        Estudios estudiosEs = estudiosSearchRepository.findOne(testEstudios.getId());
        assertThat(estudiosEs).isEqualToComparingFieldByField(testEstudios);
    }

    @Test
    @Transactional
    public void getAllEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        // Get all the estudios
        restEstudiosMockMvc.perform(get("/api/estudios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estudios.getId().intValue())))
                .andExpect(jsonPath("$.[*].centro").value(hasItem(DEFAULT_CENTRO.toString())))
                .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO_STR)))
                .andExpect(jsonPath("$.[*].fechaFinal").value(hasItem(DEFAULT_FECHA_FINAL_STR)))
                .andExpect(jsonPath("$.[*].actualmente").value(hasItem(DEFAULT_ACTUALMENTE.booleanValue())))
                .andExpect(jsonPath("$.[*].curso").value(hasItem(DEFAULT_CURSO.toString())))
                .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA.doubleValue())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].archivosContentType").value(hasItem(DEFAULT_ARCHIVOS_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].archivos").value(hasItem(Base64Utils.encodeToString(DEFAULT_ARCHIVOS))))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        // Get the estudios
        restEstudiosMockMvc.perform(get("/api/estudios/{id}", estudios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estudios.getId().intValue()))
            .andExpect(jsonPath("$.centro").value(DEFAULT_CENTRO.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO_STR))
            .andExpect(jsonPath("$.fechaFinal").value(DEFAULT_FECHA_FINAL_STR))
            .andExpect(jsonPath("$.actualmente").value(DEFAULT_ACTUALMENTE.booleanValue()))
            .andExpect(jsonPath("$.curso").value(DEFAULT_CURSO.toString()))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA.doubleValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.archivosContentType").value(DEFAULT_ARCHIVOS_CONTENT_TYPE))
            .andExpect(jsonPath("$.archivos").value(Base64Utils.encodeToString(DEFAULT_ARCHIVOS)))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEstudios() throws Exception {
        // Get the estudios
        restEstudiosMockMvc.perform(get("/api/estudios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);
        estudiosSearchRepository.save(estudios);
        int databaseSizeBeforeUpdate = estudiosRepository.findAll().size();

        // Update the estudios
        Estudios updatedEstudios = estudiosRepository.findOne(estudios.getId());
        updatedEstudios
                .centro(UPDATED_CENTRO)
                .fechaInicio(UPDATED_FECHA_INICIO)
                .fechaFinal(UPDATED_FECHA_FINAL)
                .actualmente(UPDATED_ACTUALMENTE)
                .curso(UPDATED_CURSO)
                .nota(UPDATED_NOTA)
                .descripcion(UPDATED_DESCRIPCION)
                .archivos(UPDATED_ARCHIVOS)
                .archivosContentType(UPDATED_ARCHIVOS_CONTENT_TYPE)
                .link(UPDATED_LINK);

        restEstudiosMockMvc.perform(put("/api/estudios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEstudios)))
                .andExpect(status().isOk());

        // Validate the Estudios in the database
        List<Estudios> estudios = estudiosRepository.findAll();
        assertThat(estudios).hasSize(databaseSizeBeforeUpdate);
        Estudios testEstudios = estudios.get(estudios.size() - 1);
        assertThat(testEstudios.getCentro()).isEqualTo(UPDATED_CENTRO);
        assertThat(testEstudios.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testEstudios.getFechaFinal()).isEqualTo(UPDATED_FECHA_FINAL);
        assertThat(testEstudios.isActualmente()).isEqualTo(UPDATED_ACTUALMENTE);
        assertThat(testEstudios.getCurso()).isEqualTo(UPDATED_CURSO);
        assertThat(testEstudios.getNota()).isEqualTo(UPDATED_NOTA);
        assertThat(testEstudios.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEstudios.getArchivos()).isEqualTo(UPDATED_ARCHIVOS);
        assertThat(testEstudios.getArchivosContentType()).isEqualTo(UPDATED_ARCHIVOS_CONTENT_TYPE);
        assertThat(testEstudios.getLink()).isEqualTo(UPDATED_LINK);

        // Validate the Estudios in ElasticSearch
        Estudios estudiosEs = estudiosSearchRepository.findOne(testEstudios.getId());
        assertThat(estudiosEs).isEqualToComparingFieldByField(testEstudios);
    }

    @Test
    @Transactional
    public void deleteEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);
        estudiosSearchRepository.save(estudios);
        int databaseSizeBeforeDelete = estudiosRepository.findAll().size();

        // Get the estudios
        restEstudiosMockMvc.perform(delete("/api/estudios/{id}", estudios.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean estudiosExistsInEs = estudiosSearchRepository.exists(estudios.getId());
        assertThat(estudiosExistsInEs).isFalse();

        // Validate the database is empty
        List<Estudios> estudios = estudiosRepository.findAll();
        assertThat(estudios).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);
        estudiosSearchRepository.save(estudios);

        // Search the estudios
        restEstudiosMockMvc.perform(get("/api/_search/estudios?query=id:" + estudios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estudios.getId().intValue())))
            .andExpect(jsonPath("$.[*].centro").value(hasItem(DEFAULT_CENTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO_STR)))
            .andExpect(jsonPath("$.[*].fechaFinal").value(hasItem(DEFAULT_FECHA_FINAL_STR)))
            .andExpect(jsonPath("$.[*].actualmente").value(hasItem(DEFAULT_ACTUALMENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].curso").value(hasItem(DEFAULT_CURSO.toString())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA.doubleValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].archivosContentType").value(hasItem(DEFAULT_ARCHIVOS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].archivos").value(hasItem(Base64Utils.encodeToString(DEFAULT_ARCHIVOS))))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }
}
