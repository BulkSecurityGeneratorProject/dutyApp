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
import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.repository.ServiceRepository;

/**
 * Test class for the ServiceResource REST controller.
 *
 * @see ServiceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceResourceTest {

    private static final String DEFAULT_USER_ID = "SAMPLE_TEXT";
    private static final String UPDATED_USER_ID = "UPDATED_TEXT";
    private static final String DEFAULT_SERVICE_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_SERVICE_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_API_KEY = "SAMPLE_TEXT";
    private static final String UPDATED_API_KEY = "UPDATED_TEXT";

    private static final Integer DEFAULT_SERVICE_TYPE = 0;
    private static final Integer UPDATED_SERVICE_TYPE = 1;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Inject
    private ServiceRepository serviceRepository;

    private MockMvc restServiceMockMvc;

    private Service service;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceResource serviceResource = new ServiceResource();
        ReflectionTestUtils.setField(serviceResource, "serviceRepository", serviceRepository);
        this.restServiceMockMvc = MockMvcBuilders.standaloneSetup(serviceResource).build();
    }

    @Before
    public void initTest() {
        service = new Service();
        service.setService_name(DEFAULT_SERVICE_NAME);
        service.setApi_key(DEFAULT_API_KEY);
        service.setService_type(DEFAULT_SERVICE_TYPE);
        service.setIs_deleted(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createService() throws Exception {
        // Validate the database is empty
        assertThat(serviceRepository.findAll()).hasSize(0);

        // Create the Service
        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isCreated());

        // Validate the Service in the database
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(1);
        Service testService = services.iterator().next();
        assertThat(testService.getService_name()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testService.getApi_key()).isEqualTo(DEFAULT_API_KEY);
        assertThat(testService.getService_type()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testService.getIs_deleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void getAllServices() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Get all the services
        restServiceMockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(service.getId()))
                .andExpect(jsonPath("$.[0].user_id").value(DEFAULT_USER_ID.toString()))
                .andExpect(jsonPath("$.[0].service_name").value(DEFAULT_SERVICE_NAME.toString()))
                .andExpect(jsonPath("$.[0].api_key").value(DEFAULT_API_KEY.toString()))
                .andExpect(jsonPath("$.[0].service_type").value(DEFAULT_SERVICE_TYPE))
                .andExpect(jsonPath("$.[0].is_deleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Get the service
        restServiceMockMvc.perform(get("/api/services/{id}", service.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(service.getId()))
            .andExpect(jsonPath("$.user_id").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.service_name").value(DEFAULT_SERVICE_NAME.toString()))
            .andExpect(jsonPath("$.api_key").value(DEFAULT_API_KEY.toString()))
            .andExpect(jsonPath("$.service_type").value(DEFAULT_SERVICE_TYPE))
            .andExpect(jsonPath("$.is_deleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingService() throws Exception {
        // Get the service
        restServiceMockMvc.perform(get("/api/services/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Update the service
        service.setService_name(UPDATED_SERVICE_NAME);
        service.setApi_key(UPDATED_API_KEY);
        service.setService_type(UPDATED_SERVICE_TYPE);
        service.setIs_deleted(UPDATED_IS_DELETED);
        restServiceMockMvc.perform(put("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isOk());

        // Validate the Service in the database
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(1);
        Service testService = services.iterator().next();
        assertThat(testService.getService_name()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testService.getApi_key()).isEqualTo(UPDATED_API_KEY);
        assertThat(testService.getService_type()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testService.getIs_deleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void deleteService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Get the service
        restServiceMockMvc.perform(delete("/api/services/{id}", service.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(0);
    }
}
