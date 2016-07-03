package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstancePolicy;
import com.mozhotels.srit.repository.InstancePolicyRepository;
import com.mozhotels.srit.repository.search.InstancePolicySearchRepository;
import com.mozhotels.srit.web.rest.util.HeaderUtil;
import com.mozhotels.srit.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InstancePolicy.
 */
@RestController
@RequestMapping("/api")
public class InstancePolicyResource {

    private final Logger log = LoggerFactory.getLogger(InstancePolicyResource.class);
        
    @Inject
    private InstancePolicyRepository instancePolicyRepository;
    
    @Inject
    private InstancePolicySearchRepository instancePolicySearchRepository;
    
    /**
     * POST  /instance-policies : Create a new instancePolicy.
     *
     * @param instancePolicy the instancePolicy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instancePolicy, or with status 400 (Bad Request) if the instancePolicy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-policies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstancePolicy> createInstancePolicy(@Valid @RequestBody InstancePolicy instancePolicy) throws URISyntaxException {
        log.debug("REST request to save InstancePolicy : {}", instancePolicy);
        if (instancePolicy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instancePolicy", "idexists", "A new instancePolicy cannot already have an ID")).body(null);
        }
        InstancePolicy result = instancePolicyRepository.save(instancePolicy);
        instancePolicySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-policies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instancePolicy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-policies : Updates an existing instancePolicy.
     *
     * @param instancePolicy the instancePolicy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instancePolicy,
     * or with status 400 (Bad Request) if the instancePolicy is not valid,
     * or with status 500 (Internal Server Error) if the instancePolicy couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-policies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstancePolicy> updateInstancePolicy(@Valid @RequestBody InstancePolicy instancePolicy) throws URISyntaxException {
        log.debug("REST request to update InstancePolicy : {}", instancePolicy);
        if (instancePolicy.getId() == null) {
            return createInstancePolicy(instancePolicy);
        }
        InstancePolicy result = instancePolicyRepository.save(instancePolicy);
        instancePolicySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instancePolicy", instancePolicy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-policies : get all the instancePolicies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instancePolicies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-policies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstancePolicy>> getAllInstancePolicies(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstancePolicies");
        Page<InstancePolicy> page = instancePolicyRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-policies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-policies/:id : get the "id" instancePolicy.
     *
     * @param id the id of the instancePolicy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instancePolicy, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-policies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstancePolicy> getInstancePolicy(@PathVariable Long id) {
        log.debug("REST request to get InstancePolicy : {}", id);
        InstancePolicy instancePolicy = instancePolicyRepository.findOne(id);
        return Optional.ofNullable(instancePolicy)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-policies/:id : delete the "id" instancePolicy.
     *
     * @param id the id of the instancePolicy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-policies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstancePolicy(@PathVariable Long id) {
        log.debug("REST request to delete InstancePolicy : {}", id);
        instancePolicyRepository.delete(id);
        instancePolicySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instancePolicy", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-policies?query=:query : search for the instancePolicy corresponding
     * to the query.
     *
     * @param query the query of the instancePolicy search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-policies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstancePolicy>> searchInstancePolicies(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstancePolicies for query {}", query);
        Page<InstancePolicy> page = instancePolicySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-policies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
