package com.g200001.dutyapp.web.rest;

import com.g200001.dutyapp.Application;
import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.repository.EscalationPolicyRepository;

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
 * Test class for the EscalationPolicyResource REST controller.
 *
 * @see EscalationPolicyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EscalationPolicyResourceTest {

    private static final String DEFAULT_POLICY_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_POLICY_NAME = "UPDATED_TEXT";

    private static final Boolean DEFAULT_HAS_CYCLE = false;
    private static final Boolean UPDATED_HAS_CYCLE = true;

    private static final Long DEFAULT_CYCLE_TIME = 0L;
    private static final Long UPDATED_CYCLE_TIME = 1L;

    @Inject
    private EscalationPolicyRepository escalationPolicyRepository;

    private MockMvc restEscalationPolicyMockMvc;

    private EscalationPolicy escalationPolicy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EscalationPolicyResource escalationPolicyResource = new EscalationPolicyResource();
        ReflectionTestUtils.setField(escalationPolicyResource, "escalationPolicyRepository", escalationPolicyRepository);
        this.restEscalationPolicyMockMvc = MockMvcBuilders.standaloneSetup(escalationPolicyResource).build();
    }

    @Before
    public void initTest() {
        escalationPolicy = new EscalationPolicy();
        escalationPolicy.setPolicy_name(DEFAULT_POLICY_NAME);
        escalationPolicy.setHas_cycle(DEFAULT_HAS_CYCLE);
        escalationPolicy.setCycle_time(DEFAULT_CYCLE_TIME);
    }

    @Test
    @Transactional
    public void createEscalationPolicy() throws Exception {
        // Validate the database is empty
        assertThat(escalationPolicyRepository.findAll()).hasSize(0);

        // Create the EscalationPolicy
        restEscalationPolicyMockMvc.perform(post("/api/escalationPolicys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(escalationPolicy)))
                .andExpect(status().isCreated());

        // Validate the EscalationPolicy in the database
        List<EscalationPolicy> escalationPolicys = escalationPolicyRepository.findAll();
        assertThat(escalationPolicys).hasSize(1);
        EscalationPolicy testEscalationPolicy = escalationPolicys.iterator().next();
        assertThat(testEscalationPolicy.getPolicy_name()).isEqualTo(DEFAULT_POLICY_NAME);
        assertThat(testEscalationPolicy.getHas_cycle()).isEqualTo(DEFAULT_HAS_CYCLE);
        assertThat(testEscalationPolicy.getCycle_time()).isEqualTo(DEFAULT_CYCLE_TIME);
    }

    @Test
    @Transactional
    public void getAllEscalationPolicys() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Get all the escalationPolicys
        restEscalationPolicyMockMvc.perform(get("/api/escalationPolicys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(escalationPolicy.getId().intValue()))
                .andExpect(jsonPath("$.[0].policy_name").value(DEFAULT_POLICY_NAME.toString()))
                .andExpect(jsonPath("$.[0].has_cycle").value(DEFAULT_HAS_CYCLE.booleanValue()))
                .andExpect(jsonPath("$.[0].cycle_time").value(DEFAULT_CYCLE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getEscalationPolicy() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Get the escalationPolicy
        restEscalationPolicyMockMvc.perform(get("/api/escalationPolicys/{id}", escalationPolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(escalationPolicy.getId().intValue()))
            .andExpect(jsonPath("$.policy_name").value(DEFAULT_POLICY_NAME.toString()))
            .andExpect(jsonPath("$.has_cycle").value(DEFAULT_HAS_CYCLE.booleanValue()))
            .andExpect(jsonPath("$.cycle_time").value(DEFAULT_CYCLE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEscalationPolicy() throws Exception {
        // Get the escalationPolicy
        restEscalationPolicyMockMvc.perform(get("/api/escalationPolicys/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEscalationPolicy() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Update the escalationPolicy
        escalationPolicy.setPolicy_name(UPDATED_POLICY_NAME);
        escalationPolicy.setHas_cycle(UPDATED_HAS_CYCLE);
        escalationPolicy.setCycle_time(UPDATED_CYCLE_TIME);
        restEscalationPolicyMockMvc.perform(put("/api/escalationPolicys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(escalationPolicy)))
                .andExpect(status().isOk());

        // Validate the EscalationPolicy in the database
        List<EscalationPolicy> escalationPolicys = escalationPolicyRepository.findAll();
        assertThat(escalationPolicys).hasSize(1);
        EscalationPolicy testEscalationPolicy = escalationPolicys.iterator().next();
        assertThat(testEscalationPolicy.getPolicy_name()).isEqualTo(UPDATED_POLICY_NAME);
        assertThat(testEscalationPolicy.getHas_cycle()).isEqualTo(UPDATED_HAS_CYCLE);
        assertThat(testEscalationPolicy.getCycle_time()).isEqualTo(UPDATED_CYCLE_TIME);
    }

    @Test
    @Transactional
    public void deleteEscalationPolicy() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Get the escalationPolicy
        restEscalationPolicyMockMvc.perform(delete("/api/escalationPolicys/{id}", escalationPolicy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EscalationPolicy> escalationPolicys = escalationPolicyRepository.findAll();
        assertThat(escalationPolicys).hasSize(0);
    }
}
