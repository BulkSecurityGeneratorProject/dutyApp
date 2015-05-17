package com.g200001.dutyapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
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
import com.g200001.dutyapp.domain.Alert;

import com.g200001.dutyapp.domain.User;

import com.g200001.dutyapp.domain.EscalationPolicy;
import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.domain.PolicyRule;

import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.domain.User;

import com.g200001.dutyapp.repository.AlertRepository;
import com.g200001.dutyapp.repository.IncidentRepository;
import com.g200001.dutyapp.repository.PolicyRuleRepository;
import com.g200001.dutyapp.repository.ServiceRepository;

//import com.g200001.dutyapp.web.rest.IncidentBL.IncidentCreateThread;

import com.g200001.dutyapp.repository.UserRepository;

import com.g200001.dutyapp.web.rest.util.PaginationUtil;



/**
 * REST controller for managing Incident.
 */
@RestController
@RequestMapping("/api")
public class IncidentResource {

    private final Logger log = LoggerFactory.getLogger(IncidentResource.class);

    @Inject

    private IncidentRepository incidentRepository;
    @Inject
    private PolicyRuleRepository policyRuleRepository;
    @Inject
    private ServiceRepository serviceRepository;
    @Inject
    private AlertRepository alertRepository;
    @Inject
    private UserRepository userRepository;
    
    //
	private class CreateAlertThread extends Thread {
		private final Logger log = LoggerFactory
				.getLogger(IncidentResource.class);

		private Incident _incident;
		private List<Alert> _alerts;

		public CreateAlertThread(Incident incident) {
			_incident = incident;
			_alerts = new ArrayList<>();
		}

		public void run() {
			EscalationPolicy escalationPolicy = _incident.getService()
					.getEscalationPolicy();
			
			if (escalationPolicy == null) 
				throw new RuntimeException("Escalation Policy NULL");
			
			for (PolicyRule rule: escalationPolicy.getPolicyRules()) {
				for(User user: rule.getUsers()) {
					Alert alert = new Alert();
					alert.setIncident(_incident);
					alert.setUser(user);
					
					alert.setAlert_time(DateTime.now().plusMinutes(
							rule.getEscalate_time()));
					
					_alerts.add(alert);
				}
			} 
			
			alertRepository.save(_alerts);
			

    	}
    }
   
    

    /**
     * POST  /incidents -> Create a new incident.
     */
    @RequestMapping(value = "/incidents",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Incident incident) throws URISyntaxException {
        log.debug("REST request to save Incident : {}", incident);
        if (incident.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new incident cannot already have an ID").build();
        }
        
        // TO-DO: should we set a assign user here??
        // incident.setAssign_user(assign_user);
        incident.setCreate_time(DateTime.now());
        incident.setState(Incident.CREATED);
        
        Service s = serviceRepository.findOne(incident.getService().getId());
        incident.setService(s);
        
        incidentRepository.save(incident);


        new CreateAlertThread(incident).run();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("incidentID", incident.getId());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        //return ResponseEntity.created(new URI("/api/incidents/" + incident.getId())).build();

//        new IncidentCreateThread(incident).run();
//        return ResponseEntity.created(new URI("/api/incidents/" + incident.getId())).build();
//>>>>>>> master
    }

    /**
     * PUT  /incidents/:id -> Updates an existing incident.
     */
    @RequestMapping(value = "/incidents/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Incident incident) throws URISyntaxException {
        log.debug("REST request to update Incident : {}", incident);
        
        Incident i = incidentRepository.findOne(id);
        if (i.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
      
        incident.setId(id);
        i = incident;
        incidentRepository.save(i);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /incidents -> get all the incidents.
     */
    @RequestMapping(value = "/incidents",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Incident>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Incident> page = incidentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/incidents", offset, limit);
        return new ResponseEntity<List<Incident>>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /incidents/new -> get all the new incidents.
     */
    @RequestMapping(value = "/incidents/New/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Incident>> getNew(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Incident> page = incidentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/incidents", offset, limit);
        return new ResponseEntity<List<Incident>>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /incidents/:id -> get the "id" incident.
     */
    @RequestMapping(value = "/incidents/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Incident> get(@PathVariable String id, HttpServletResponse response) {
        log.debug("REST request to get Incident : {}", id);
        Incident incident = incidentRepository.findOne(id);
        if (incident == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(incident, HttpStatus.OK);
    }

    /**
     * DELETE  /incidents/:id -> delete the "id" incident.
     */
    @RequestMapping(value = "/incidents/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete Incident : {}", id);
        
      //delete all the Alerts that belongs to the incidents
        Incident incident = incidentRepository.findOne(id);
        List<Alert> alerts = alertRepository.findAllByIncident(incident);
        alertRepository.delete(alerts);
        
        incidentRepository.delete(id);
    }
    
    /**
     * PUT  /incidents/:id/resolve -> Resolve an existing incident.
     */
    @RequestMapping(value = "/incidents/{id}/resolve",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> resolve(@PathVariable String id, @RequestBody String userId) throws URISyntaxException {
        log.debug("REST request to resolve Incident : {}", id);
        
        Incident incident = incidentRepository.findOne(id);
        if (incident.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        User user = userRepository.findOne(userId);
        if (user == null)
        	throw new RuntimeException("User should not be NULL");
        
        incident.setResolve_user(user);
        incident.setResolve_time(DateTime.now());
        incident.setState(Incident.RESOLVED);
        
        incidentRepository.save(incident);
        return ResponseEntity.ok().build();
    }
    
    /**
     * PUT  /incidents/:id/acknowledge -> acknowledge an existing incident.
     */
    @RequestMapping(value = "/incidents/{id}/acknowledge",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> acknowledge(@PathVariable String id, @RequestBody String userId) throws URISyntaxException {
    	log.debug("REST request to acknowledge Incident : {}", id);
        
        Incident incident = incidentRepository.findOne(id);        

        if (incident.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        User user = userRepository.findOne(userId);
        if (user == null)
        	throw new RuntimeException("User should not be NULL");
        
        incident.setAck_user(user);
        incident.setAck_time(DateTime.now());
        incident.setState(Incident.ACKNOWLEDGED);
        
        incidentRepository.save(incident);
        return ResponseEntity.ok().build();
    }
    
    /**
     * PUT  /incidents/:id/assign/:useId -> assign an existing incident.
     */
    @RequestMapping(value = "/incidents/{id}/reassign/{assignToUserId}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> assign(@PathVariable String id, @PathVariable String assignToUserId, @RequestBody String userId) throws URISyntaxException {
    	log.debug("REST request to assign Incident : {}", id);
        
        Incident incident = incidentRepository.findOne(id);        

        if (incident.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        User user = userRepository.findOne(userId);
        if (user == null)
        	throw new RuntimeException("User should not be NULL");
        
        User assignUsr = userRepository.findOne(assignToUserId);
        if (assignUsr == null)
        	throw new RuntimeException("User should not be NULL");
        
        incident.setAssign_user(assignUsr);
        incident.setAck_time(DateTime.now());
        incident.setState(Incident.REASSIGNED);
        
        incidentRepository.save(incident);
        return ResponseEntity.ok().build();
    }
}
