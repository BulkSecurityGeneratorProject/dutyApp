package com.g200001.dutyapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.domain.PolicyRule;
import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.domain.User;
import com.g200001.dutyapp.repository.EscalationPolicyRepository;
import com.g200001.dutyapp.repository.IncidentRepository;
import com.g200001.dutyapp.repository.ServiceRepository;
import com.g200001.dutyapp.repository.UserRepository;

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
    private static final String DEFAULT_SERVICE_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_SERVICE_NAME = "UPDATED_TEXT";

    private static final Integer DEFAULT_SERVICE_TYPE = 0;
    private static final Integer UPDATED_SERVICE_TYPE = 1;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;
    
    @Inject
    private ServiceRepository serviceRepository;
    @Inject
    private ServiceResource serviceResource;
    @Inject
    private IncidentRepository incidentRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private EscalationPolicyRepository escalationPolicyRepository;

    private MockMvc restServiceMockMvc;

    private Service service;
    private Incident incident;
    
    private int initServiceNum = 0;
    private int initIncidentNum = 0;
    private int initPolicyNum = 0;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //ServiceResource serviceResource = new ServiceResource();
        ReflectionTestUtils.setField(serviceResource, "serviceRepository", serviceRepository);
        this.restServiceMockMvc = MockMvcBuilders.standaloneSetup(serviceResource).build();
        
    	initServiceNum = serviceRepository.findAll().size();
    	initIncidentNum = incidentRepository.findAll().size();
    	initPolicyNum = escalationPolicyRepository.findAll().size();
    }

    @Before
    public void initTest() {    	
    	// Initialize to insert one EscalationPolicy
    	EscalationPolicy escalationPolicy = new EscalationPolicy();
        escalationPolicy.setPolicy_name("ServiceTest Escalate Policy");
        escalationPolicy.setHas_cycle(false);
        
        Set<PolicyRule> policyRules = new HashSet<PolicyRule>();        
        
        Set<User> users = new HashSet<User>();
        for (User usr: userRepository.findAll())
        	users.add(usr);
        
        PolicyRule rule1 = new PolicyRule();
        rule1.setSequence(0);
        rule1.setUsers(users);
        policyRules.add(rule1);
        
        escalationPolicy.setPolicyRules(policyRules);
        escalationPolicyRepository.saveAndFlush(escalationPolicy);
        
        //--------------------------------
        // Create Service Object
        service = new Service();
        service.setService_name(DEFAULT_SERVICE_NAME);
        service.setService_type(DEFAULT_SERVICE_TYPE);
        service.setIs_deleted(false);
        service.setApi_key("###");
                    
        //find existing Escalate Policy and set it to serivce
        EscalationPolicy e = escalationPolicyRepository.findOne(escalationPolicy.getId());
        service.setEscalationPolicy(e);
    }  
    
    @Test
    @Transactional
    public void createService() throws Exception {
        // Validate the database is empty
        assertThat(serviceRepository.findAll()).hasSize(initServiceNum);
        assertThat(incidentRepository.findAll()).hasSize(initIncidentNum);

        // Create the Service
        restServiceMockMvc.perform(post("/api/services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isCreated());

        // Validate the Service in the database
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(initServiceNum + 1);
        Service testService = services.get(initServiceNum);
        
        assertThat(testService.getService_name()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testService.getService_type()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testService.getApi_key()).isNotEqualTo(null);
        assertThat(testService.getIs_deleted()).isEqualTo(DEFAULT_IS_DELETED);
        
        // Validate escalationPolicy can be accessed through service  
        assertThat(testService.getEscalationPolicy().getPolicy_name())
        	.isEqualTo("ServiceTest Escalate Policy");
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
                .andExpect(jsonPath("$.[" + initServiceNum + "].id").value(service.getId()))
                .andExpect(jsonPath("$.[" + initServiceNum + "].service_name").value(DEFAULT_SERVICE_NAME.toString()))
                .andExpect(jsonPath("$.[" + initServiceNum + "].api_key").exists())
                .andExpect(jsonPath("$.[" + initServiceNum + "].service_type").value(DEFAULT_SERVICE_TYPE))
                .andExpect(jsonPath("$.[" + initServiceNum + "].is_deleted").value(DEFAULT_IS_DELETED.booleanValue()));
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
            .andExpect(jsonPath("$.service_name").value(DEFAULT_SERVICE_NAME.toString()))
            .andExpect(jsonPath("$.api_key").exists())
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
        service.setService_type(UPDATED_SERVICE_TYPE);
        service.setIs_deleted(UPDATED_IS_DELETED);
        restServiceMockMvc.perform(put("/api/services/{id}", service.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isOk());

        // Validate the Service in the database
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(initServiceNum + 1);
        Service testService = services.get(initServiceNum);
        
        assertThat(testService.getService_name()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testService.getService_type()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testService.getIs_deleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    /* When delete the service, it should deletes all the incidents that
     * belongs to the service; but should not delete the escalate policy 
     */
    
    @Test
    @Transactional
    public void deleteService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // create an Incident and save
        incident = new Incident();
        incident.setDescription("Test Incident");
        incident.setCreate_time(new DateTime(0L, DateTimeZone.UTC));
        incident.setAck_time(new DateTime(0L, DateTimeZone.UTC));
        incident.setResolve_time(new DateTime(0L, DateTimeZone.UTC));
        incident.setService(service); 
        incidentRepository.saveAndFlush(incident);
        
        //validate the incident is in database now
        List<Service> services = serviceRepository.findAll();
        assertThat(services).hasSize(initServiceNum + 1);
        List<Incident> incidents = incidentRepository.findAll();
        assertThat(incidents).hasSize(initIncidentNum + 1);
        
        // Get the service
        restServiceMockMvc.perform(delete("/api/services/{id}", service.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        services = serviceRepository.findAll();
        assertThat(services).hasSize(initServiceNum);
        incidents = incidentRepository.findAll();
        assertThat(incidents).hasSize(initIncidentNum);
        
        //Escapolicy should be not be deleted
        List<EscalationPolicy> escalationPolicys = escalationPolicyRepository.findAll();
        assertThat(escalationPolicys).hasSize(initPolicyNum + 1);
    }
    
    @Test
    @Transactional
    public void disableService() throws Exception {
    	// Initialize the database
    	service.setIs_deleted(false);
        serviceRepository.saveAndFlush(service);
        
        // Validate the service is enable now
        service = serviceRepository.findOne(service.getId());
        assertThat(service.getIs_deleted()).isEqualTo(false);
        
     // Get the service
        restServiceMockMvc.perform(put("/api/services/{id}/disable", service.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        
     // Validate the service is enable now
        service = serviceRepository.findOne(service.getId());
        assertThat(service.getIs_deleted()).isEqualTo(true);       
    }
    
    @Test
    @Transactional
    public void enableService() throws Exception {
    	// Initialize the database
    	service.setIs_deleted(true);
        serviceRepository.saveAndFlush(service);
        
        // Validate the service is enable now
        service = serviceRepository.findOne(service.getId());
        assertThat(service.getIs_deleted()).isEqualTo(true);
        
     // Get the service
        restServiceMockMvc.perform(put("/api/services/{id}/enable", service.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        
     // Validate the service is enable now
        service = serviceRepository.findOne(service.getId());
        assertThat(service.getIs_deleted()).isEqualTo(false);       
    }
}