package com.g200001.dutyapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

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
        
        System.out.println(escalationPolicy.getPolicyRules().size());
        Iterator<PolicyRule> it = escalationPolicy.getPolicyRules().iterator();
        System.out.println(it.next().getUsers().size());
        
        escalationPolicyRepository.save(escalationPolicy);
        return ResponseEntity.created(new URI("/api/escalationPolicys/" + escalationPolicy.getId())).build();
    }

    /**
     * PUT  /escalationPolicys -> Updates an existing escalationPolicy.
     */
    @RequestMapping(value = "/escalationPolicys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody EscalationPolicy escalationPolicy) throws URISyntaxException {
        log.debug("REST request to update EscalationPolicy : {}", escalationPolicy);
        if (escalationPolicy.getId() == null) {
            return create(escalationPolicy);
        }
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
}
