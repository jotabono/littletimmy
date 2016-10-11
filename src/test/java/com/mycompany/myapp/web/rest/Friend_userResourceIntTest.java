package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LittletimmyApp;

import com.mycompany.myapp.domain.Friend_user;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.Friend_userRepository;
import com.mycompany.myapp.repository.search.Friend_userSearchRepository;

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
 * Test class for the Friend_userResource REST controller.
 *
 * @see Friend_userResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LittletimmyApp.class)
public class Friend_userResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_FRIENDSHIP = false;
    private static final Boolean UPDATED_FRIENDSHIP = true;

    private static final ZonedDateTime DEFAULT_FRIENDSHIP_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FRIENDSHIP_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FRIENDSHIP_DATE_STR = dateTimeFormatter.format(DEFAULT_FRIENDSHIP_DATE);

    @Inject
    private Friend_userRepository friend_userRepository;

    @Inject
    private Friend_userSearchRepository friend_userSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFriend_userMockMvc;

    private Friend_user friend_user;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Friend_userResource friend_userResource = new Friend_userResource();
        ReflectionTestUtils.setField(friend_userResource, "friend_userSearchRepository", friend_userSearchRepository);
        ReflectionTestUtils.setField(friend_userResource, "friend_userRepository", friend_userRepository);
        this.restFriend_userMockMvc = MockMvcBuilders.standaloneSetup(friend_userResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friend_user createEntity(EntityManager em) {
        Friend_user friend_user = new Friend_user()
                .friendship(DEFAULT_FRIENDSHIP)
                .friendship_date(DEFAULT_FRIENDSHIP_DATE);
        // Add required entity
        User friend_from = UserResourceIntTest.createEntity(em);
        em.persist(friend_from);
        em.flush();
        friend_user.setFriend_from(friend_from);
        // Add required entity
        User friend_to = UserResourceIntTest.createEntity(em);
        em.persist(friend_to);
        em.flush();
        friend_user.setFriend_to(friend_to);
        return friend_user;
    }

    @Before
    public void initTest() {
        friend_userSearchRepository.deleteAll();
        friend_user = createEntity(em);
    }

    @Test
    @Transactional
    public void createFriend_user() throws Exception {
        int databaseSizeBeforeCreate = friend_userRepository.findAll().size();

        // Create the Friend_user

        restFriend_userMockMvc.perform(post("/api/friend-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(friend_user)))
                .andExpect(status().isCreated());

        // Validate the Friend_user in the database
        List<Friend_user> friend_users = friend_userRepository.findAll();
        assertThat(friend_users).hasSize(databaseSizeBeforeCreate + 1);
        Friend_user testFriend_user = friend_users.get(friend_users.size() - 1);
        assertThat(testFriend_user.isFriendship()).isEqualTo(DEFAULT_FRIENDSHIP);
        assertThat(testFriend_user.getFriendship_date()).isEqualTo(DEFAULT_FRIENDSHIP_DATE);

        // Validate the Friend_user in ElasticSearch
        Friend_user friend_userEs = friend_userSearchRepository.findOne(testFriend_user.getId());
        assertThat(friend_userEs).isEqualToComparingFieldByField(testFriend_user);
    }

    @Test
    @Transactional
    public void getAllFriend_users() throws Exception {
        // Initialize the database
        friend_userRepository.saveAndFlush(friend_user);

        // Get all the friend_users
        restFriend_userMockMvc.perform(get("/api/friend-users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(friend_user.getId().intValue())))
                .andExpect(jsonPath("$.[*].friendship").value(hasItem(DEFAULT_FRIENDSHIP.booleanValue())))
                .andExpect(jsonPath("$.[*].friendship_date").value(hasItem(DEFAULT_FRIENDSHIP_DATE_STR)));
    }

    @Test
    @Transactional
    public void getFriend_user() throws Exception {
        // Initialize the database
        friend_userRepository.saveAndFlush(friend_user);

        // Get the friend_user
        restFriend_userMockMvc.perform(get("/api/friend-users/{id}", friend_user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(friend_user.getId().intValue()))
            .andExpect(jsonPath("$.friendship").value(DEFAULT_FRIENDSHIP.booleanValue()))
            .andExpect(jsonPath("$.friendship_date").value(DEFAULT_FRIENDSHIP_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFriend_user() throws Exception {
        // Get the friend_user
        restFriend_userMockMvc.perform(get("/api/friend-users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriend_user() throws Exception {
        // Initialize the database
        friend_userRepository.saveAndFlush(friend_user);
        friend_userSearchRepository.save(friend_user);
        int databaseSizeBeforeUpdate = friend_userRepository.findAll().size();

        // Update the friend_user
        Friend_user updatedFriend_user = friend_userRepository.findOne(friend_user.getId());
        updatedFriend_user
                .friendship(UPDATED_FRIENDSHIP)
                .friendship_date(UPDATED_FRIENDSHIP_DATE);

        restFriend_userMockMvc.perform(put("/api/friend-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFriend_user)))
                .andExpect(status().isOk());

        // Validate the Friend_user in the database
        List<Friend_user> friend_users = friend_userRepository.findAll();
        assertThat(friend_users).hasSize(databaseSizeBeforeUpdate);
        Friend_user testFriend_user = friend_users.get(friend_users.size() - 1);
        assertThat(testFriend_user.isFriendship()).isEqualTo(UPDATED_FRIENDSHIP);
        assertThat(testFriend_user.getFriendship_date()).isEqualTo(UPDATED_FRIENDSHIP_DATE);

        // Validate the Friend_user in ElasticSearch
        Friend_user friend_userEs = friend_userSearchRepository.findOne(testFriend_user.getId());
        assertThat(friend_userEs).isEqualToComparingFieldByField(testFriend_user);
    }

    @Test
    @Transactional
    public void deleteFriend_user() throws Exception {
        // Initialize the database
        friend_userRepository.saveAndFlush(friend_user);
        friend_userSearchRepository.save(friend_user);
        int databaseSizeBeforeDelete = friend_userRepository.findAll().size();

        // Get the friend_user
        restFriend_userMockMvc.perform(delete("/api/friend-users/{id}", friend_user.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean friend_userExistsInEs = friend_userSearchRepository.exists(friend_user.getId());
        assertThat(friend_userExistsInEs).isFalse();

        // Validate the database is empty
        List<Friend_user> friend_users = friend_userRepository.findAll();
        assertThat(friend_users).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFriend_user() throws Exception {
        // Initialize the database
        friend_userRepository.saveAndFlush(friend_user);
        friend_userSearchRepository.save(friend_user);

        // Search the friend_user
        restFriend_userMockMvc.perform(get("/api/_search/friend-users?query=id:" + friend_user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friend_user.getId().intValue())))
            .andExpect(jsonPath("$.[*].friendship").value(hasItem(DEFAULT_FRIENDSHIP.booleanValue())))
            .andExpect(jsonPath("$.[*].friendship_date").value(hasItem(DEFAULT_FRIENDSHIP_DATE_STR)));
    }
}
