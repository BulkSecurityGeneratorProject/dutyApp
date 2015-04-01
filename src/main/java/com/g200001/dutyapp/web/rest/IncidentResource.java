package com.g200001.dutyapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
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
import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.domain.PolicyRule;
import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.repository.IncidentRepository;
import com.g200001.dutyapp.repository.PolicyRuleRepository;
import com.g200001.dutyapp.repository.ServiceRepository;
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
    
    //
    private class CreateAlertThread extends Thread
    {
    	private final Logger log = LoggerFactory.getLogger(IncidentResource.class);
    	
    	private Incident _incident;
    	public CreateAlertThread(Incident incident)
    	{
    		System.out.print("thread structure serviceRepository equals null: " );
    		System.out.println( serviceRepository==null);
    		_incident = incident;
    	}
    	public void run(){
    		AlertResource alertresource = new AlertResource();
    		Service service = _incident.getService();
    		String id = service.getId();
    		
    		System.out.print("before call serviceRepository equals null: " );
    		System.out.println(serviceRepository==null);
    		
    		Service _service =serviceRepository.findOne(id);
    		EscalationPolicy  escalationPolicy = _service.getEscalationPolicy();
    		if (escalationPolicy!=null)
    		{
    			PolicyRule rule=policyRuleRepository.findOneByEscalationPolicyAndSequence(escalationPolicy, 1);
        		if (rule != null)
        		{
        			
        			
        		}
        		else    			
        		{
        			//throw exception
        		}
        		
    		}
    		else
    		{
    			//throw exception
    		}
//    		System.out.println(DateTime.now().toString("hh:mm:ss")+ "incident id="+_incident.getId());
//    		try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace(); 
//            }
//    		System.out.println(DateTime.now().toString("hh:mm:ss")+ "incident id="+_incident.getId());
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
        System.out.print(" Thread serviceRepository equals null: ");
        System.out.println(serviceRepository==null);
        System.out.println(incidentRepository==null);
        System.out.println(policyRuleRepository==null);
        
        incidentRepository.save(incident);
        System.out.print("before Thread serviceRepository equals null: ");
        System.out.println(serviceRepository==null);
        new CreateAlertThread(incident).run();
        return ResponseEntity.created(new URI("/api/incidents/" + incident.getId())).build();
    }

    /**
     * PUT  /incidents -> Updates an existing incident.
     */
    @RequestMapping(value = "/incidents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Incident incident) throws URISyntaxException {
        log.debug("REST request to update Incident : {}", incident);
        if (incident.getId() == null) {
            return create(incident);
        }
        incidentRepository.save(incident);
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
        incidentRepository.delete(id);
    }
}
