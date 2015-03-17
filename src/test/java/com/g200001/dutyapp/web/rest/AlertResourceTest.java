package com.g200001.dutyapp.web.rest;

import com.g200001.dutyapp.Application;
import com.g200001.dutyapp.domain.Alert;
import com.g200001.dutyapp.repository.AlertRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AlertResource REST controller.
 *
 * @see AlertResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AlertResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_ALERT_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ALERT_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ALERT_TIME_STR = dateTimeFormatter.print(DEFAULT_ALERT_TIME);

    @Inject
    private AlertRepository alertRepository;

    private MockMvc restAlertMockMvc;

    private Alert alert;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlertResource alertResource = new AlertResource();
        ReflectionTestUtils.setField(alertResource, "alertRepository", alertRepository);
        this.restAlertMockMvc = MockMvcBuilders.standaloneSetup(alertResource).build();
    }

    @Before
    public void initTest() {
        alert = new Alert();
        alert.setAlert_time(DEFAULT_ALERT_TIME);
    }

    @Test
    @Transactional
    public void createAlert() throws Exception {
        // Validate the database is empty
        assertThat(alertRepository.findAll()).hasSize(0);

        // Create the Alert
        restAlertMockMvc.perform(post("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(alert)))
                .andExpect(status().isCreated());

        // Validate the Alert in the database
        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(1);
        Alert testAlert = alerts.iterator().next();
        assertThat(testAlert.getAlert_time().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ALERT_TIME);
    }

    @Test
    @Transactional
    public void getAllAlerts() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alerts
        restAlertMockMvc.perform(get("/api/alerts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(alert.getId()))
                .andExpect(jsonPath("$.[0].alert_time").value(DEFAULT_ALERT_TIME_STR));
    }

    @Test
    @Transactional
    public void getAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get the alert
        restAlertMockMvc.perform(get("/api/alerts/{id}", alert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(alert.getId()))
            .andExpect(jsonPath("$.alert_time").value(DEFAULT_ALERT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAlert() throws Exception {
        // Get the alert
        restAlertMockMvc.perform(get("/api/alerts/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Update the alert
        alert.setAlert_time(UPDATED_ALERT_TIME);
        restAlertMockMvc.perform(put("/api/alerts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(alert)))
                .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(1);
        Alert testAlert = alerts.iterator().next();
        assertThat(testAlert.getAlert_time().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ALERT_TIME);
    }

    @Test
    @Transactional
    public void deleteAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get the alert
        restAlertMockMvc.perform(delete("/api/alerts/{id}", alert.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Alert> alerts = alertRepository.findAll();
        assertThat(alerts).hasSize(0);
    }
}
