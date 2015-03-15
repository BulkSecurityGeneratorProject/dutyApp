package com.g200001.dutyapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.g200001.dutyapp.domain.BillAccount;
import com.g200001.dutyapp.repository.BillAccountRepository;
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
 * REST controller for managing BillAccount.
 */
@RestController
@RequestMapping("/api")
public class BillAccountResource {

    private final Logger log = LoggerFactory.getLogger(BillAccountResource.class);

    @Inject
    private BillAccountRepository billAccountRepository;

    /**
     * POST  /billAccounts -> Create a new billAccount.
     */
    @RequestMapping(value = "/billAccounts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody BillAccount billAccount) throws URISyntaxException {
        log.debug("REST request to save BillAccount : {}", billAccount);
        if (billAccount.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new billAccount cannot already have an ID").build();
        }
        billAccountRepository.save(billAccount);
        return ResponseEntity.created(new URI("/api/billAccounts/" + billAccount.getId())).build();
    }

    /**
     * PUT  /billAccounts -> Updates an existing billAccount.
     */
    @RequestMapping(value = "/billAccounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody BillAccount billAccount) throws URISyntaxException {
        log.debug("REST request to update BillAccount : {}", billAccount);
        if (billAccount.getId() == null) {
            return create(billAccount);
        }
        billAccountRepository.save(billAccount);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /billAccounts -> get all the billAccounts.
     */
    @RequestMapping(value = "/billAccounts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BillAccount>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<BillAccount> page = billAccountRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/billAccounts", offset, limit);
        return new ResponseEntity<List<BillAccount>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /billAccounts/:id -> get the "id" billAccount.
     */
    @RequestMapping(value = "/billAccounts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillAccount> get(@PathVariable String id, HttpServletResponse response) {
        log.debug("REST request to get BillAccount : {}", id);
        BillAccount billAccount = billAccountRepository.findOne(id);
        if (billAccount == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(billAccount, HttpStatus.OK);
    }

    /**
     * DELETE  /billAccounts/:id -> delete the "id" billAccount.
     */
    @RequestMapping(value = "/billAccounts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete BillAccount : {}", id);
        billAccountRepository.delete(id);
    }
}
