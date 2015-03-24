package com.g200001.dutyapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
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
import com.g200001.dutyapp.domain.PolicyRule;
import com.g200001.dutyapp.domain.User;
import com.g200001.dutyapp.repository.PolicyRuleRepository;
import com.g200001.dutyapp.web.rest.util.PaginationUtil;

/**
 * REST controller for managing PolicyRule.
 */
@RestController
@RequestMapping("/api")
public class PolicyRuleResource {

    private final Logger log = LoggerFactory.getLogger(PolicyRuleResource.class);

    @Inject
    private PolicyRuleRepository policyRuleRepository;

    /**
     * POST  /policyRules -> Create a new policyRule.
     */
    @RequestMapping(value = "/policyRules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody PolicyRule policyRule) throws URISyntaxException {
        log.debug("REST request to save PolicyRule : {}", policyRule);
        if (policyRule.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new policyRule cannot already have an ID").build();
        }
        policyRuleRepository.save(policyRule);
        return ResponseEntity.created(new URI("/api/policyRules/" + policyRule.getId())).build();
    }

    /**
     * PUT  /policyRules -> Updates an existing policyRule.
     */
    @RequestMapping(value = "/policyRules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody PolicyRule policyRule) throws URISyntaxException {
        log.debug("REST request to update PolicyRule : {}", policyRule);
        if (policyRule.getId() == null) {
            return create(policyRule);
        }
        policyRuleRepository.save(policyRule);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /policyRules -> get all the policyRules.
     */
    @RequestMapping(value = "/policyRules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PolicyRule>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<PolicyRule> page = policyRuleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/policyRules", offset, limit);
        return new ResponseEntity<List<PolicyRule>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /policyRules/:id -> get the "id" policyRule.
     */
    @RequestMapping(value = "/policyRules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyRule> get(@PathVariable String id, HttpServletResponse response) {
        log.debug("REST request to get PolicyRule : {}", id);
        //PolicyRule policyRule = policyRuleRepository.findOne(id);
        
      //-------- test part 
        Set<User> users = new HashSet<>();
        users.add(new User("111", "test1@test.com", "test1"));
        users.add(new User("222", "test2@test.com", "test2"));
        PolicyRule policyRule = new PolicyRule();
        policyRule.setId("00001");;
        policyRule.setEscalate_time(10);
        policyRule.setUsers(users);
      //--------------
        
        if (policyRule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(policyRule, HttpStatus.OK);
    }

    /**
     * DELETE  /policyRules/:id -> delete the "id" policyRule.
     */
    @RequestMapping(value = "/policyRules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete PolicyRule : {}", id);
        policyRuleRepository.delete(id);
    }
}
