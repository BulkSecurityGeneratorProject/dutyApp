package com.g200001.dutyapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.domain.PolicyRule;
import com.g200001.dutyapp.repository.EscalationPolicyRepository;
import com.g200001.dutyapp.repository.PolicyRuleRepository;
import com.g200001.dutyapp.web.rest.util.PaginationUtil;

/**
 * REST controller for managing EscalationPolicy.
 */
@RestController
@RequestMapping("/api")
public class EscalationPolicyResource {

    private final Logger log = LoggerFactory.getLogger(EscalationPolicyResource.class);

    @Inject
    private EscalationPolicyRepository escalationPolicyRepository;
    @Inject
    private PolicyRuleRepository policyRuleRepository ;

    /**
     * POST  /escalationPolicys -> Create a new escalationPolicy.
     */
    @RequestMapping(value = "/escalationPolicys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody EscalationPolicy escalationPolicy) throws URISyntaxException {
        log.debug("REST request to save EscalationPolicy : {}", escalationPolicy);
        if (escalationPolicy.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new escalationPolicy cannot already have an ID").build();
        }       
        
        for(PolicyRule rule: escalationPolicy.getPolicyRules())
        	rule.setEscalationPolicy(escalationPolicy);
        
        escalationPolicyRepository.saveAndFlush(escalationPolicy);
        HttpHeaders headers = new HttpHeaders();
        headers.set("policyID", escalationPolicy.getId());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        //return ResponseEntity.created(new URI("/api/escalationPolicys/" + escalationPolicy.getId())).build();
    }

    /**
     * PUT  /escalationPolicys/:id -> Updates an existing escalationPolicy.
     */
    @RequestMapping(value = "/escalationPolicys/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody EscalationPolicy escalationPolicy) throws URISyntaxException {
        log.debug("REST request to update EscalationPolicy : {}", escalationPolicy);
        
        EscalationPolicy e = escalationPolicyRepository.findOne(id);
        if (e.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        for(PolicyRule rule: escalationPolicy.getPolicyRules())
        	rule.setEscalationPolicy(escalationPolicy);
        
        escalationPolicyRepository.save(escalationPolicy);
        return ResponseEntity.ok().build();
    }
    
    
    /**
     * GET  /escalationPolicys -> get all the escalationPolicys.
     */
    @RequestMapping(value = "/escalationPolicys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EscalationPolicy>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<EscalationPolicy> page = escalationPolicyRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/escalationPolicys", offset, limit);
        return new ResponseEntity<List<EscalationPolicy>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /escalationPolicys/:id -> get the "id" escalationPolicy.
     */
    @RequestMapping(value = "/escalationPolicys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EscalationPolicy> get(@PathVariable String id, HttpServletResponse response) {
        log.debug("REST request to get EscalationPolicy : {}", id);
        EscalationPolicy escalationPolicy = escalationPolicyRepository.findOne(id);
        if (escalationPolicy == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(escalationPolicy, HttpStatus.OK);       
    }

    /**
     * DELETE  /escalationPolicys/:id -> delete the "id" escalationPolicy.
     */
    @RequestMapping(value = "/escalationPolicys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete EscalationPolicy : {}", id);
        
        escalationPolicyRepository.delete(id);
    }
    
    
    /////// Policy Rule ////////////
    /**
     * POST  /escalationPolicys/:policyId/policyRules -> Append a new policyRule.
     */
    @RequestMapping(value = "/escalationPolicys/{policyId}/policyRules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> appendRule(@PathVariable String policyId, @RequestBody PolicyRule policyRule) throws URISyntaxException {
        log.debug("REST request to append policy rule: {}", policyRule);
        if (policyRule.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new escalationPolicy cannot already have an ID").build();
        }
        
        EscalationPolicy escalationPolicy = escalationPolicyRepository.findOne(policyId);
        Set<PolicyRule> policyRules = escalationPolicy.getPolicyRules();
        policyRule.setEscalationPolicy(escalationPolicy);
        policyRules.add(policyRule);

        policyRuleRepository.save(policyRule);
        return ResponseEntity.created(new URI("/api/escalationPolicys/" + policyId + "/policyRules/" 
        		+ policyRule.getId())).build();
    }
    
    /**
     * PUT  /escalationPolicys/:policyId/policyRules/ -> Updates existing Policy Rules.
     */
    @RequestMapping(value = "/escalationPolicys/{policyId}/policyRules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> updateRule(@PathVariable String policyId,
    		 @RequestBody Set<PolicyRule> policyRules) throws URISyntaxException {
        log.debug("REST request to update Policy Rules : {}", policyId);
        
        EscalationPolicy escalationPolicy = escalationPolicyRepository.findOne(policyId);
        
        if (escalationPolicy == null) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Set<PolicyRule> rules = escalationPolicy.getPolicyRules();
        rules.clear();
                       
        for(PolicyRule rule : policyRules) {
        	rule.setEscalationPolicy(escalationPolicy);
        	rules.add(rule);
        }
              
        escalationPolicyRepository.save(escalationPolicy);
        return ResponseEntity.ok().build();
    }
    
    /**
     * PUT  /escalationPolicys/:policyId/policyRules/:id -> Updates an existing Policy Rule.
     */
    @RequestMapping(value = "/escalationPolicys/{policyId}/policyRules/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> updateRule(@PathVariable String policyId,
    		@PathVariable String id, @RequestBody PolicyRule policyRule) throws URISyntaxException {
        log.debug("REST request to update Policy Rule : {}", id);
        
        EscalationPolicy escalationPolicy = escalationPolicyRepository.findOne(policyId);
        if (escalationPolicy == null) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        PolicyRule rule = policyRuleRepository.findOne(id);
        
        if (rule.getEscalationPolicy() != escalationPolicy)
        	throw new RuntimeException("policy.rule does not equal to rule id");
        
        policyRule.setId(id);
        policyRule.setEscalationPolicy(escalationPolicy);
        rule = policyRule;
        
        policyRuleRepository.save(rule);
        return ResponseEntity.ok().build();
    }
    
    /**
     * GET  /escalationPolicys/:policyId/policyRules -> get all the Policy Rules.
     */
    @RequestMapping(value = "/escalationPolicys/{policyId}/policyRules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PolicyRule>> getAllRules( @PathVariable String policyId,
    								@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
    	EscalationPolicy escalationPolicy = escalationPolicyRepository.findOne(policyId);
    	
        Page<PolicyRule> page = 
        		policyRuleRepository.findByEscalationPolicy(escalationPolicy, 
        				PaginationUtil.generatePageRequest(offset, limit));
        System.out.println("Page is null? " + page==null);
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, 
        		"/api/escalationPolicys/" + policyId + "/policyRules", offset, limit);
        return new ResponseEntity<List<PolicyRule>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /escalationPolicys/:policyId/policyRules/:id -> get the "id" Policy Rule.
     */
    @RequestMapping(value = "/escalationPolicys/{policyId}/policyRules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyRule> getOneRule(
    		@PathVariable String policyId, @PathVariable String id, 
    		HttpServletResponse response) throws Exception {
        log.debug("REST request to get Policy Rule : {}", id);
        /*EscalationPolicy escalationPolicy = escalationPolicyRepository.findOne(policyId);
        if (escalationPolicy == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        PolicyRule rule = policyRuleRepository.findOne(id);
        
        if (!rule.getEscalationPolicy().equals(escalationPolicy))
        	throw new RuntimeException("policy.rule does not equal to rule id");
        
        ResponseEntity<PolicyRule> r = new ResponseEntity<>(rule, HttpStatus.OK);
        System.out.println("---------------");
        System.out.println(r.getHeaders());
        System.out.println(r.getBody());
        return r;*/
        
        PolicyRule rule = policyRuleRepository.findOne(id);
        return new ResponseEntity<PolicyRule>(rule, HttpStatus.OK);
    }
    
    /**
     * DELETE  /escalationPolicys/:policyId/policyRules/:id -> delete the "id" Policy Rule.
     */
    @RequestMapping(value = "/escalationPolicys/{policyId}/policyRules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void deleteOneRule(@PathVariable String policyId, @PathVariable String id) {
        log.debug("REST request to delete Policy Rule: {}", id);
        
        EscalationPolicy escalationPolicy = escalationPolicyRepository.findOne(policyId);
        if (escalationPolicy == null) {
        	throw new RuntimeException("escalationPolicy should not be null");
        }
        
        PolicyRule rule = policyRuleRepository.findOne(id);
        
        if (rule.getEscalationPolicy() != escalationPolicy)
        	throw new RuntimeException("policy.rule does not equal to rule id");
        
        policyRuleRepository.delete(id);
    }
}
