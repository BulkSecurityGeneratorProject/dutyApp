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
import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.repository.IncidentRepository;
import com.g200001.dutyapp.repository.ServiceRepository;
import com.g200001.dutyapp.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Service.
 */
@RestController
@RequestMapping("/api")
public class ServiceResource {

    private final Logger log = LoggerFactory.getLogger(ServiceResource.class);

    @Inject
    private ServiceRepository serviceRepository;
    @Inject
    private IncidentRepository incidentRepository;

    /**
     * POST  /services -> Create a new service.
     */
    @RequestMapping(value = "/services",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Service service) throws URISyntaxException {
        log.debug("REST request to save Service : {}", service);
        if (service.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new service cannot already have an ID").build();
        }
        
        //when created, is_delete always is false
        service.setIs_deleted(false);
        
        //TO-DO: make the api key
        service.setApi_key("###");
        
        serviceRepository.save(service);
        
        return ResponseEntity.created(new URI("/api/services/" + service.getId())).build();
    }

    /**
     * PUT  /services -> Updates an existing service.
     */
    @RequestMapping(value = "/services/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Service service) throws URISyntaxException {
        log.debug("REST request to update Service : {}", service);
        
        Service s = serviceRepository.findOne(id);
        if (s.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        service.setId(id);
        s = service;
        serviceRepository.save(s);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /services -> get all the services.
     */
    @RequestMapping(value = "/services",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Service>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Service> page = serviceRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/services", offset, limit);
        return new ResponseEntity<List<Service>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /services/:id -> get the "id" service.
     */
    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Service> get(@PathVariable String id, HttpServletResponse response) {
        log.debug("REST request to get Service : {}", id);
        Service service = serviceRepository.findOne(id);
        if (service == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    /**
     * DELETE  /services/:id -> delete the "id" service.
     */
    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete Service : {}", id);
        //delete all the incidents that belongs to the service
        Service s = serviceRepository.findOne(id);
        List<Incident> incidents = incidentRepository.findByService(s);
        incidentRepository.delete(incidents);
        
        serviceRepository.delete(id);
    }
    
    /**
     * PUT /services/:id/disable  -> logical delete
     */
    @RequestMapping(value = "/services/{id}/disable",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> disable(@PathVariable String id) {
    	log.debug("REST request to disable Service : {}", id);
    	 Service service = serviceRepository.findOne(id);
    	 service.setIs_deleted(true);
    	 serviceRepository.save(service);
    	 
    	 return ResponseEntity.ok().build();
    }
    
    /**
     * PUT /services/:id/enable  -> logical resume
     */
    @RequestMapping(value = "/services/{id}/enable",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> enable(@PathVariable String id) {
    	log.debug("REST request to disable Service : {}", id);
    	 Service service = serviceRepository.findOne(id);
    	 service.setIs_deleted(false);
    	 serviceRepository.save(service);
    	 
    	 return ResponseEntity.ok().build();
    }
}
