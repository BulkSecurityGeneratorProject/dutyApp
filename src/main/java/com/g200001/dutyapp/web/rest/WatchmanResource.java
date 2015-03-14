package com.g200001.dutyapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.g200001.dutyapp.domain.Watchman;
import com.g200001.dutyapp.repository.WatchmanRepository;
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
 * REST controller for managing Watchman.
 */
@RestController
@RequestMapping("/api")
public class WatchmanResource {

    private final Logger log = LoggerFactory.getLogger(WatchmanResource.class);

    @Inject
    private WatchmanRepository watchmanRepository;

    /**
     * POST  /watchmans -> Create a new watchman.
     */
    @RequestMapping(value = "/watchmans",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Watchman watchman) throws URISyntaxException {
        log.debug("REST request to save Watchman : {}", watchman);
        if (watchman.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new watchman cannot already have an ID").build();
        }
        watchmanRepository.save(watchman);
        return ResponseEntity.created(new URI("/api/watchmans/" + watchman.getId())).build();
    }

    /**
     * PUT  /watchmans -> Updates an existing watchman.
     */
    @RequestMapping(value = "/watchmans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Watchman watchman) throws URISyntaxException {
        log.debug("REST request to update Watchman : {}", watchman);
        if (watchman.getId() == null) {
            return create(watchman);
        }
        watchmanRepository.save(watchman);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /watchmans -> get all the watchmans.
     */
    @RequestMapping(value = "/watchmans",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Watchman>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Watchman> page = watchmanRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/watchmans", offset, limit);
        return new ResponseEntity<List<Watchman>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /watchmans/:id -> get the "id" watchman.
     */
    @RequestMapping(value = "/watchmans/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Watchman> get(@PathVariable String id, HttpServletResponse response) {
        log.debug("REST request to get Watchman : {}", id);
        Watchman watchman = watchmanRepository.findOne(id);
        if (watchman == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(watchman, HttpStatus.OK);
    }

    /**
     * DELETE  /watchmans/:id -> delete the "id" watchman.
     */
    @RequestMapping(value = "/watchmans/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete Watchman : {}", id);
        watchmanRepository.delete(id);
    }
}
