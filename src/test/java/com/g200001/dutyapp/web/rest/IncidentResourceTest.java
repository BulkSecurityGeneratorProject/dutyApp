package com.g200001.dutyapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

import com.g200001.dutyapp.Application;
import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.repository.IncidentRepository;



/**
 * Test class for the IncidentResource REST controller.
 *
 * @see IncidentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class IncidentResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_CREATE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATE_TIME_STR = dateTimeFormatter.print(DEFAULT_CREATE_TIME);

    private static final Integer DEFAULT_STATE = 0;
    private static final Integer UPDATED_STATE = 1;

    private static final DateTime DEFAULT_ACK_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ACK_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ACK_TIME_STR = dateTimeFormatter.print(DEFAULT_ACK_TIME);

    private static final DateTime DEFAULT_RESOLVE_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_RESOLVE_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_RESOLVE_TIME_STR = dateTimeFormatter.print(DEFAULT_RESOLVE_TIME);
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_DETAIL = "SAMPLE_TEXT";
    private static final String UPDATED_DETAIL = "UPDATED_TEXT";

    private static final Long DEFAULT_INCIDENT_NO = 0L;
    private static final Long UPDATED_INCIDENT_NO = 1L;

    @Inject
    private IncidentRepository incidentRepository;
    @Inject
    private IncidentResource incidentResource;
    
    
    private MockMvc restIncidentMockMvc;

    private Incident incident;
    private Service service;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //IncidentResource incidentResource = new IncidentResource();
        ReflectionTestUtils.setField(incidentResource, "incidentRepository", incidentRepository);
        this.restIncidentMockMvc = MockMvcBuilders.standaloneSetup(incidentResource).build();
    }

    @Before
    public void initTest() {
        incident = new Incident();
        incident.setCreate_time(DEFAULT_CREATE_TIME);
        incident.setState(DEFAULT_STATE);
        incident.setAck_time(DEFAULT_ACK_TIME);
        incident.setResolve_time(DEFAULT_RESOLVE_TIME);
        incident.setDescription(DEFAULT_DESCRIPTION);
        incident.setDetail(DEFAULT_DETAIL);
        incident.setIncident_no(DEFAULT_INCIDENT_NO);
        
        service = new Service();
        service.setService_name("Service Test");
        incident.setService(service);
        
    }

    @Test
    @Transactional
    public void createIncident() throws Exception {
        // Validate the database is empty
        assertThat(incidentRepository.findAll()).hasSize(0);
        
        // Create the Incident
        restIncidentMockMvc.perform(post("/api/incidents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incident)))
                .andExpect(status().isCreated());

        // Validate the Incident in the database
        List<Incident> incidents = incidentRepository.findAll();
        assertThat(incidents).hasSize(1);
        Incident testIncident = incidents.iterator().next();
        assertThat(testIncident.getCreate_time().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIncident.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testIncident.getAck_time().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ACK_TIME);
        assertThat(testIncident.getResolve_time().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_RESOLVE_TIME);
        assertThat(testIncident.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIncident.getDetail()).isEqualTo(DEFAULT_DETAIL);
        assertThat(testIncident.getIncident_no()).isEqualTo(DEFAULT_INCIDENT_NO);
    }

    @Test
    @Transactional
    public void getAllIncidents() throws Exception {
        // Initialize the database
        incidentRepository.saveAndFlush(incident);

        // Get all the incidents
        restIncidentMockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(incident.getId()))
                .andExpect(jsonPath("$.[0].create_time").value(DEFAULT_CREATE_TIME_STR))
                .andExpect(jsonPath("$.[0].state").value(DEFAULT_STATE))
                .andExpect(jsonPath("$.[0].ack_time").value(DEFAULT_ACK_TIME_STR))
                .andExpect(jsonPath("$.[0].resolve_time").value(DEFAULT_RESOLVE_TIME_STR))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.[0].detail").value(DEFAULT_DETAIL.toString()))
                .andExpect(jsonPath("$.[0].incident_no").value(DEFAULT_INCIDENT_NO.intValue()));
    }

    @Test
    @Transactional
    public void getIncident() throws Exception {
        // Initialize the database
        incidentRepository.saveAndFlush(incident);

        // Get the incident
        restIncidentMockMvc.perform(get("/api/incidents/{id}", incident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(incident.getId()))
            .andExpect(jsonPath("$.create_time").value(DEFAULT_CREATE_TIME_STR))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.ack_time").value(DEFAULT_ACK_TIME_STR))
            .andExpect(jsonPath("$.resolve_time").value(DEFAULT_RESOLVE_TIME_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL.toString()))
            .andExpect(jsonPath("$.incident_no").value(DEFAULT_INCIDENT_NO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIncident() throws Exception {
        // Get the incident
        restIncidentMockMvc.perform(get("/api/incidents/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncident() throws Exception {
        // Initialize the database
        incidentRepository.saveAndFlush(incident);

        // Update the incident
        incident.setCreate_time(UPDATED_CREATE_TIME);
        incident.setState(UPDATED_STATE);
        incident.setAck_time(UPDATED_ACK_TIME);
        incident.setResolve_time(UPDATED_RESOLVE_TIME);
        incident.setDescription(UPDATED_DESCRIPTION);
        incident.setDetail(UPDATED_DETAIL);
        incident.setIncident_no(UPDATED_INCIDENT_NO);
        restIncidentMockMvc.perform(put("/api/incidents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incident)))
                .andExpect(status().isOk());

        // Validate the Incident in the database
        List<Incident> incidents = incidentRepository.findAll();
        assertThat(incidents).hasSize(1);
        Incident testIncident = incidents.iterator().next();
        assertThat(testIncident.getCreate_time().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIncident.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testIncident.getAck_time().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ACK_TIME);
        assertThat(testIncident.getResolve_time().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_RESOLVE_TIME);
        assertThat(testIncident.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIncident.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testIncident.getIncident_no()).isEqualTo(UPDATED_INCIDENT_NO);
    }

    @Test
    @Transactional
    public void deleteIncident() throws Exception {
        // Initialize the database
        incidentRepository.saveAndFlush(incident);

        // Get the incident
        restIncidentMockMvc.perform(delete("/api/incidents/{id}", incident.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Incident> incidents = incidentRepository.findAll();
        assertThat(incidents).hasSize(0);
    }
}