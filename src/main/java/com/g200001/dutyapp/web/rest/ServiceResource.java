package com.g200001.dutyapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.g200001.dutyapp.domain.Service;
import com.g200001.dutyapp.repository.ServiceRepository;
import com.g200001.dutyapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Service.
 */
@RestController
@RequestMapping("/api")
public class ServiceResource {

    private final Logger log = LoggerFactory.getLogger(ServiceResource.class);

    @Inject
    private ServiceRepository serviceRepository;

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
        serviceRepository.save(service);
        return ResponseEntity.created(new URI("/api/services/" + service.getId())).build();
    }

    /**
     * PUT  /services -> Updates an existing service.
     */
    @RequestMapping(value = "/services",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Service service) throws URISyntaxException {
        log.debug("REST request to update Service : {}", service);
        if (service.getId() == null) {
            return create(service);
        }
        serviceRepository.save(service);
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
        serviceRepository.delete(id);
    }
}
