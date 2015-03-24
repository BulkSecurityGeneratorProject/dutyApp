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
import com.g200001.dutyapp.domain.Alert;
import com.g200001.dutyapp.repository.AlertRepository;
import com.g200001.dutyapp.web.rest.util.PaginationUtil;


/**
 * REST controller for managing Alert.
 */
@RestController
@RequestMapping("/api")
public class AlertResource {

    private final Logger log = LoggerFactory.getLogger(AlertResource.class);

    @Inject
    private AlertRepository alertRepository;

    /**
     * POST  /alerts -> Create a new alert.
     */
    @RequestMapping(value = "/alerts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Alert alert) throws URISyntaxException {
        log.debug("REST request to save Alert : {}", alert);
        if (alert.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new alert cannot already have an ID").build();
        }
        alertRepository.save(alert);
        return ResponseEntity.created(new URI("/api/alerts/" + alert.getId())).build();
    }

    /**
     * PUT  /alerts -> Updates an existing alert.
     */
    @RequestMapping(value = "/alerts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Alert alert) throws URISyntaxException {
        log.debug("REST request to update Alert : {}", alert);
        if (alert.getId() == null) {
            return create(alert);
        }
        alertRepository.save(alert);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /alerts -> get all the alerts.
     */
    @RequestMapping(value = "/alerts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Alert>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Alert> page = alertRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alerts", offset, limit);
        return new ResponseEntity<List<Alert>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alerts/:id -> get the "id" alert.
     */
    @RequestMapping(value = "/alerts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Alert> get(@PathVariable String id, HttpServletResponse response) {
        log.debug("REST request to get Alert : {}", id);
        Alert alert = alertRepository.findOne(id);
        if (alert == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(alert, HttpStatus.OK);
    }

    /**
     * DELETE  /alerts/:id -> delete the "id" alert.
     */
    @RequestMapping(value = "/alerts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete Alert : {}", id);
        alertRepository.delete(id);
    }
}
