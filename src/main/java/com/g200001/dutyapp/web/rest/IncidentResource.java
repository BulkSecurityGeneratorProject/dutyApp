package com.g200001.dutyapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.g200001.dutyapp.domain.Incident;
import com.g200001.dutyapp.repository.IncidentRepository;
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
 * REST controller for managing Incident.
 */
@RestController
@RequestMapping("/api")
public class IncidentResource {

    private final Logger log = LoggerFactory.getLogger(IncidentResource.class);

    @Inject
    private IncidentRepository incidentRepository;

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
        incidentRepository.save(incident);
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
    public ResponseEntity<Incident> get(@PathVariable Long id, HttpServletResponse response) {
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
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Incident : {}", id);
        incidentRepository.delete(id);
    }
}
