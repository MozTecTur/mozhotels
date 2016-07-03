package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstanceFacility;
import com.mozhotels.srit.repository.InstanceFacilityRepository;
import com.mozhotels.srit.repository.search.InstanceFacilitySearchRepository;
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
 * REST controller for managing InstanceFacility.
 */
@RestController
@RequestMapping("/api")
public class InstanceFacilityResource {

    private final Logger log = LoggerFactory.getLogger(InstanceFacilityResource.class);
        
    @Inject
    private InstanceFacilityRepository instanceFacilityRepository;
    
    @Inject
    private InstanceFacilitySearchRepository instanceFacilitySearchRepository;
    
    /**
     * POST  /instance-facilities : Create a new instanceFacility.
     *
     * @param instanceFacility the instanceFacility to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceFacility, or with status 400 (Bad Request) if the instanceFacility has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-facilities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceFacility> createInstanceFacility(@Valid @RequestBody InstanceFacility instanceFacility) throws URISyntaxException {
        log.debug("REST request to save InstanceFacility : {}", instanceFacility);
        if (instanceFacility.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceFacility", "idexists", "A new instanceFacility cannot already have an ID")).body(null);
        }
        InstanceFacility result = instanceFacilityRepository.save(instanceFacility);
        instanceFacilitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-facilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceFacility", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-facilities : Updates an existing instanceFacility.
     *
     * @param instanceFacility the instanceFacility to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceFacility,
     * or with status 400 (Bad Request) if the instanceFacility is not valid,
     * or with status 500 (Internal Server Error) if the instanceFacility couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-facilities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceFacility> updateInstanceFacility(@Valid @RequestBody InstanceFacility instanceFacility) throws URISyntaxException {
        log.debug("REST request to update InstanceFacility : {}", instanceFacility);
        if (instanceFacility.getId() == null) {
            return createInstanceFacility(instanceFacility);
        }
        InstanceFacility result = instanceFacilityRepository.save(instanceFacility);
        instanceFacilitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceFacility", instanceFacility.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-facilities : get all the instanceFacilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instanceFacilities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-facilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceFacility>> getAllInstanceFacilities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstanceFacilities");
        Page<InstanceFacility> page = instanceFacilityRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-facilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-facilities/:id : get the "id" instanceFacility.
     *
     * @param id the id of the instanceFacility to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceFacility, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-facilities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceFacility> getInstanceFacility(@PathVariable Long id) {
        log.debug("REST request to get InstanceFacility : {}", id);
        InstanceFacility instanceFacility = instanceFacilityRepository.findOne(id);
        return Optional.ofNullable(instanceFacility)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-facilities/:id : delete the "id" instanceFacility.
     *
     * @param id the id of the instanceFacility to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-facilities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceFacility(@PathVariable Long id) {
        log.debug("REST request to delete InstanceFacility : {}", id);
        instanceFacilityRepository.delete(id);
        instanceFacilitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceFacility", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-facilities?query=:query : search for the instanceFacility corresponding
     * to the query.
     *
     * @param query the query of the instanceFacility search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-facilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceFacility>> searchInstanceFacilities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstanceFacilities for query {}", query);
        Page<InstanceFacility> page = instanceFacilitySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-facilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
