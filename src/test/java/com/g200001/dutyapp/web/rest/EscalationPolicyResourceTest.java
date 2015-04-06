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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.g200001.dutyapp.Application;
import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.domain.PolicyRule;
import com.g200001.dutyapp.domain.User;
import com.g200001.dutyapp.repository.EscalationPolicyRepository;
import com.g200001.dutyapp.repository.PolicyRuleRepository;
import com.g200001.dutyapp.repository.UserRepository;

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
    @Inject
    private EscalationPolicyResource escalationPolicyResource;
    @Inject
    private PolicyRuleRepository policyRuleRepository;
    @Inject
    private UserRepository userRepository;

    private MockMvc restEscalationPolicyMockMvc;
    

    private EscalationPolicy escalationPolicy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //EscalationPolicyResource escalationPolicyResource = new EscalationPolicyResource();
        //ReflectionTestUtils.setField(escalationPolicyResource, "escalationPolicyRepository", escalationPolicyRepository);
        //ReflectionTestUtils.setField(escalationPolicyResource, "policyRuleRepository", policyRuleRepository);
        this.restEscalationPolicyMockMvc = MockMvcBuilders.standaloneSetup(escalationPolicyResource).build();
    }

    @Before
    public void initTest() {    	
    	//escalationPolicyRepository.deleteAll();
        escalationPolicy = new EscalationPolicy();
        escalationPolicy.setPolicy_name(DEFAULT_POLICY_NAME);
        escalationPolicy.setHas_cycle(DEFAULT_HAS_CYCLE);
        escalationPolicy.setCycle_time(DEFAULT_CYCLE_TIME);
        
        Set<PolicyRule> policyRules = new HashSet<PolicyRule>();        
        
        Set<User> users = new HashSet<User>();
        for (User usr: userRepository.findAll())
        	users.add(usr);
        
        PolicyRule rule1 = new PolicyRule();
        rule1.setSequence(0);
        rule1.setEscalate_time(10);
        rule1.setUsers(users);
        rule1.setEscalationPolicy(escalationPolicy);
        policyRules.add(rule1);
        
        escalationPolicy.setPolicyRules(policyRules);
    }

    @Test
    @Transactional
    public void createEscalationPolicy() throws Exception {
        // Validate the database is empty
        assertThat(escalationPolicyRepository.findAll()).hasSize(0);
        assertThat(policyRuleRepository.findAll()).hasSize(0);        
        		 
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
        
        // Validate PolicyRule/User can be accessed through service
        assertThat(testEscalationPolicy.getPolicyRules()).hasSize(1);  
        assertThat(testEscalationPolicy.getPolicyRules().iterator().next()
        		.getUsers()).hasSize(userRepository.findAll().size());
        
        //Validate the Policy Rule in the database
        List<PolicyRule> rules = policyRuleRepository.findAll();
        assertThat(rules).hasSize(1);
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
                .andExpect(jsonPath("$.[0].id").value(escalationPolicy.getId()))
                .andExpect(jsonPath("$.[0].policy_name").value(DEFAULT_POLICY_NAME.toString()))
                .andExpect(jsonPath("$.[0].has_cycle").value(DEFAULT_HAS_CYCLE.booleanValue()))
                .andExpect(jsonPath("$.[0].cycle_time").value(DEFAULT_CYCLE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getEscalationPolicy() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);
        
        EscalationPolicy policy = escalationPolicyRepository.findOne(escalationPolicy.getId());
        assertThat(policy.getPolicyRules()).hasSize(1);
        Iterator<PolicyRule> it = policy.getPolicyRules().iterator();
        assertThat(it.next().getUsers()).hasSize(userRepository.findAll().size());

        // Get the escalationPolicy
        restEscalationPolicyMockMvc.perform(get("/api/escalationPolicys/{id}", escalationPolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(escalationPolicy.getId()))
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
        
        restEscalationPolicyMockMvc.perform(put("/api/escalationPolicys/{id}", escalationPolicy.getId())
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

        
        List<User> users = userRepository.findAll();
        int UserCount = users.size();
        // Get the escalationPolicy
        restEscalationPolicyMockMvc.perform(delete("/api/escalationPolicys/{id}", escalationPolicy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        restEscalationPolicyMockMvc.perform(get("/api/escalationPolicys/{id}", escalationPolicy.getId()))
        .andExpect(status().isNotFound());
        
        // Validate the database is empty
//        List<EscalationPolicy> escalationPolicys = escalationPolicyRepository.findAll();
//        assertThat(escalationPolicys).hasSize(0);
//        List<PolicyRule> policyRules = policyRuleRepository.findAll();
//        assertThat(policyRules).hasSize(0);
        
        //Users should be not be deleted
         users = userRepository.findAll();
        assertThat(users).hasSize(UserCount);
    }
    
    @Test
    @Transactional
    public void appendPolicyRule() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Validate current policy rule number
        EscalationPolicy e = escalationPolicyRepository.findOne(escalationPolicy.getId());
        assertThat(e.getPolicyRules()).hasSize(1);
        assertThat(policyRuleRepository.findAll()).hasSize(1);
        
        // create new rule            
        PolicyRule newRule = new PolicyRule();
        newRule.setSequence(1);
        newRule.setEscalate_time(20);
        
        // append the Policy Rule
        restEscalationPolicyMockMvc.perform(post("/api/escalationPolicys/{policyId}/policyRules", escalationPolicy.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(newRule)))
                .andExpect(status().isCreated());

        // Validate current policy rule number
        e = escalationPolicyRepository.findOne(escalationPolicy.getId());
        assertThat(e.getPolicyRules()).hasSize(2);
        
        //validate rules number
        e = escalationPolicyRepository.findOne(escalationPolicy.getId());
        assertThat(e.getPolicyRules()).hasSize(2);
        
        // Validate from policy repository        
        List<PolicyRule> rules = policyRuleRepository.findByEscalationPolicy(e);
        assertThat(rules).hasSize(2);
    }
    
    @Test
    @Transactional
    public void updatePolicyRules() throws Exception {
    	// Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);
        
        // Validate rule's user number
        EscalationPolicy e = escalationPolicyRepository.findOne(escalationPolicy.getId());
        Set<PolicyRule> rules = e.getPolicyRules();
        assertThat(rules).hasSize(1);
        
        // create new policy rule set
        Set<PolicyRule> newRules = new HashSet<>();
        PolicyRule rule1 = new PolicyRule();
        rule1.setEscalate_time(10);
        newRules.add(rule1);
        PolicyRule rule2 = new PolicyRule();
        newRules.add(rule2);
       
        // Update the Policy rules
        restEscalationPolicyMockMvc.perform(put("/api/escalationPolicys/{policyId}/policyRules", 
        				e.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(newRules)))
                .andExpect(status().isOk());
        
      //validate rule number
        assertThat(policyRuleRepository.findByEscalationPolicy(e)).hasSize(2);
    }
    
    @Test
    @Transactional
    public void updatePolicyRule() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Validate rule's user number
        EscalationPolicy e = escalationPolicyRepository.findOne(escalationPolicy.getId());
        Set<PolicyRule> rules = e.getPolicyRules();
        assertThat(rules).hasSize(1);
        
        PolicyRule rule = rules.iterator().next();
        Set<User> users = rule.getUsers();
        assertThat(users).hasSize(4);
        
        // remove first user from the rule
        User usr = users.iterator().next();
        users.remove(usr);
      
        // Update the Policy rule
        restEscalationPolicyMockMvc.perform(put("/api/escalationPolicys/{policyId}/policyRules/{id}", 
        				e.getId(), rule.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rule)))
                .andExpect(status().isOk());
        
        //validate user number again
        e = escalationPolicyRepository.findOne(escalationPolicy.getId());
        rules = e.getPolicyRules();
        assertThat(rules.iterator().next().getUsers()).hasSize(3);
        
        //validate rule number
        assertThat(policyRuleRepository.findByEscalationPolicy(e)).hasSize(1);
    }
  
/*    
    @Test
    public void getAllPolicyRule() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Validate rule's user number
        EscalationPolicy e = escalationPolicyRepository.findAll().iterator().next();
        
     // Get all the Policy Rules
        restEscalationPolicyMockMvc.perform(get("/api/escalationPolicys/{policyId}/policyRules", e.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].sequence").value(0)) ;
    }
*/    
    @Test
    public void getPolicyRule() throws Exception {
        // Initialize the database
        escalationPolicyRepository.saveAndFlush(escalationPolicy);

        // Get the rule and policy
        EscalationPolicy e = escalationPolicyRepository.findAll().iterator().next();
        PolicyRule r = e.getPolicyRules().iterator().next();
        
        // Get all the Policy Rules
        restEscalationPolicyMockMvc.perform(get("/api/escalationPolicys/{policyId}/policyRules/{id}"
        		, e.getId() , r.getId() ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sequence").value(0));
    }
    
    @Test
    public void deletePolicyRule() throws Exception {
    	
    }
}
