package com.g200001.dutyapp.web.rest;

import com.g200001.dutyapp.Application;
import com.g200001.dutyapp.domain.Watchman;
import com.g200001.dutyapp.repository.WatchmanRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WatchmanResource REST controller.
 *
 * @see WatchmanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WatchmanResourceTest {

    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";
    private static final String DEFAULT_PASSWORD = "SAMPLE_TEXT";
    private static final String UPDATED_PASSWORD = "UPDATED_TEXT";

    @Inject
    private WatchmanRepository watchmanRepository;

    private MockMvc restWatchmanMockMvc;

    private Watchman watchman;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WatchmanResource watchmanResource = new WatchmanResource();
        ReflectionTestUtils.setField(watchmanResource, "watchmanRepository", watchmanRepository);
        this.restWatchmanMockMvc = MockMvcBuilders.standaloneSetup(watchmanResource).build();
    }

    @Before
    public void initTest() {
        watchman = new Watchman();
        watchman.setEmail(DEFAULT_EMAIL);
        watchman.setPassword(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createWatchman() throws Exception {
        // Validate the database is empty
        assertThat(watchmanRepository.findAll()).hasSize(0);

        // Create the Watchman
        restWatchmanMockMvc.perform(post("/api/watchmans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(watchman)))
                .andExpect(status().isCreated());

        // Validate the Watchman in the database
        List<Watchman> watchmans = watchmanRepository.findAll();
        assertThat(watchmans).hasSize(1);
        Watchman testWatchman = watchmans.iterator().next();
        assertThat(testWatchman.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testWatchman.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllWatchmans() throws Exception {
        // Initialize the database
        watchmanRepository.saveAndFlush(watchman);

        // Get all the watchmans
        restWatchmanMockMvc.perform(get("/api/watchmans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(watchman.getId()))
                .andExpect(jsonPath("$.[0].email").value(DEFAULT_EMAIL.toString()))
                .andExpect(jsonPath("$.[0].password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getWatchman() throws Exception {
        // Initialize the database
        watchmanRepository.saveAndFlush(watchman);

        // Get the watchman
        restWatchmanMockMvc.perform(get("/api/watchmans/{id}", watchman.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(watchman.getId()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWatchman() throws Exception {
        // Get the watchman
        restWatchmanMockMvc.perform(get("/api/watchmans/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWatchman() throws Exception {
        // Initialize the database
        watchmanRepository.saveAndFlush(watchman);

        // Update the watchman
        watchman.setEmail(UPDATED_EMAIL);
        watchman.setPassword(UPDATED_PASSWORD);
        restWatchmanMockMvc.perform(put("/api/watchmans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(watchman)))
                .andExpect(status().isOk());

        // Validate the Watchman in the database
        List<Watchman> watchmans = watchmanRepository.findAll();
        assertThat(watchmans).hasSize(1);
        Watchman testWatchman = watchmans.iterator().next();
        assertThat(testWatchman.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWatchman.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void deleteWatchman() throws Exception {
        // Initialize the database
        watchmanRepository.saveAndFlush(watchman);

        // Get the watchman
        restWatchmanMockMvc.perform(delete("/api/watchmans/{id}", watchman.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Watchman> watchmans = watchmanRepository.findAll();
        assertThat(watchmans).hasSize(0);
    }
}
