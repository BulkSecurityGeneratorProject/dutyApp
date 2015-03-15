package com.g200001.dutyapp.web.rest;

import com.g200001.dutyapp.Application;
import com.g200001.dutyapp.domain.PolicyRule;
import com.g200001.dutyapp.repository.PolicyRuleRepository;

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
 * Test class for the PolicyRuleResource REST controller.
 *
 * @see PolicyRuleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PolicyRuleResourceTest {


    private static final Integer DEFAULT_SEQUENCE = 0;
    private static final Integer UPDATED_SEQUENCE = 1;

    private static final Integer DEFAULT_ESCALATE_TIME = 0;
    private static final Integer UPDATED_ESCALATE_TIME = 1;

    @Inject
    private PolicyRuleRepository policyRuleRepository;

    private MockMvc restPolicyRuleMockMvc;

    private PolicyRule policyRule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PolicyRuleResource policyRuleResource = new PolicyRuleResource();
        ReflectionTestUtils.setField(policyRuleResource, "policyRuleRepository", policyRuleRepository);
        this.restPolicyRuleMockMvc = MockMvcBuilders.standaloneSetup(policyRuleResource).build();
    }

    @Before
    public void initTest() {
        policyRule = new PolicyRule();
        policyRule.setSequence(DEFAULT_SEQUENCE);
        policyRule.setEscalate_time(DEFAULT_ESCALATE_TIME);
    }

    @Test
    @Transactional
    public void createPolicyRule() throws Exception {
        // Validate the database is empty
        assertThat(policyRuleRepository.findAll()).hasSize(0);

        // Create the PolicyRule
        restPolicyRuleMockMvc.perform(post("/api/policyRules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(policyRule)))
                .andExpect(status().isCreated());

        // Validate the PolicyRule in the database
        List<PolicyRule> policyRules = policyRuleRepository.findAll();
        assertThat(policyRules).hasSize(1);
        PolicyRule testPolicyRule = policyRules.iterator().next();
        assertThat(testPolicyRule.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
        assertThat(testPolicyRule.getEscalate_time()).isEqualTo(DEFAULT_ESCALATE_TIME);
    }

    @Test
    @Transactional
    public void getAllPolicyRules() throws Exception {
        // Initialize the database
        policyRuleRepository.saveAndFlush(policyRule);

        // Get all the policyRules
        restPolicyRuleMockMvc.perform(get("/api/policyRules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(policyRule.getId()))
                .andExpect(jsonPath("$.[0].sequence").value(DEFAULT_SEQUENCE))
                .andExpect(jsonPath("$.[0].escalate_time").value(DEFAULT_ESCALATE_TIME));
    }

    @Test
    @Transactional
    public void getPolicyRule() throws Exception {
        // Initialize the database
        policyRuleRepository.saveAndFlush(policyRule);

        // Get the policyRule
        restPolicyRuleMockMvc.perform(get("/api/policyRules/{id}", policyRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(policyRule.getId()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE))
            .andExpect(jsonPath("$.escalate_time").value(DEFAULT_ESCALATE_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingPolicyRule() throws Exception {
        // Get the policyRule
        restPolicyRuleMockMvc.perform(get("/api/policyRules/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolicyRule() throws Exception {
        // Initialize the database
        policyRuleRepository.saveAndFlush(policyRule);

        // Update the policyRule
        policyRule.setSequence(UPDATED_SEQUENCE);
        policyRule.setEscalate_time(UPDATED_ESCALATE_TIME);
        restPolicyRuleMockMvc.perform(put("/api/policyRules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(policyRule)))
                .andExpect(status().isOk());

        // Validate the PolicyRule in the database
        List<PolicyRule> policyRules = policyRuleRepository.findAll();
        assertThat(policyRules).hasSize(1);
        PolicyRule testPolicyRule = policyRules.iterator().next();
        assertThat(testPolicyRule.getSequence()).isEqualTo(UPDATED_SEQUENCE);
        assertThat(testPolicyRule.getEscalate_time()).isEqualTo(UPDATED_ESCALATE_TIME);
    }

    @Test
    @Transactional
    public void deletePolicyRule() throws Exception {
        // Initialize the database
        policyRuleRepository.saveAndFlush(policyRule);

        // Get the policyRule
        restPolicyRuleMockMvc.perform(delete("/api/policyRules/{id}", policyRule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PolicyRule> policyRules = policyRuleRepository.findAll();
        assertThat(policyRules).hasSize(0);
    }
}
