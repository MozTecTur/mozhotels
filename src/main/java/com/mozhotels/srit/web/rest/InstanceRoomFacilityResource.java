package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstanceRoomFacility;
import com.mozhotels.srit.repository.InstanceRoomFacilityRepository;
import com.mozhotels.srit.repository.search.InstanceRoomFacilitySearchRepository;
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
 * REST controller for managing InstanceRoomFacility.
 */
@RestController
@RequestMapping("/api")
public class InstanceRoomFacilityResource {

    private final Logger log = LoggerFactory.getLogger(InstanceRoomFacilityResource.class);
        
    @Inject
    private InstanceRoomFacilityRepository instanceRoomFacilityRepository;
    
    @Inject
    private InstanceRoomFacilitySearchRepository instanceRoomFacilitySearchRepository;
    
    /**
     * POST  /instance-room-facilities : Create a new instanceRoomFacility.
     *
     * @param instanceRoomFacility the instanceRoomFacility to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceRoomFacility, or with status 400 (Bad Request) if the instanceRoomFacility has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-room-facilities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomFacility> createInstanceRoomFacility(@Valid @RequestBody InstanceRoomFacility instanceRoomFacility) throws URISyntaxException {
        log.debug("REST request to save InstanceRoomFacility : {}", instanceRoomFacility);
        if (instanceRoomFacility.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceRoomFacility", "idexists", "A new instanceRoomFacility cannot already have an ID")).body(null);
        }
        InstanceRoomFacility result = instanceRoomFacilityRepository.save(instanceRoomFacility);
        instanceRoomFacilitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-room-facilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceRoomFacility", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-room-facilities : Updates an existing instanceRoomFacility.
     *
     * @param instanceRoomFacility the instanceRoomFacility to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceRoomFacility,
     * or with status 400 (Bad Request) if the instanceRoomFacility is not valid,
     * or with status 500 (Internal Server Error) if the instanceRoomFacility couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-room-facilities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomFacility> updateInstanceRoomFacility(@Valid @RequestBody InstanceRoomFacility instanceRoomFacility) throws URISyntaxException {
        log.debug("REST request to update InstanceRoomFacility : {}", instanceRoomFacility);
        if (instanceRoomFacility.getId() == null) {
            return createInstanceRoomFacility(instanceRoomFacility);
        }
        InstanceRoomFacility result = instanceRoomFacilityRepository.save(instanceRoomFacility);
        instanceRoomFacilitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceRoomFacility", instanceRoomFacility.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-room-facilities : get all the instanceRoomFacilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instanceRoomFacilities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-room-facilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceRoomFacility>> getAllInstanceRoomFacilities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstanceRoomFacilities");
        Page<InstanceRoomFacility> page = instanceRoomFacilityRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-room-facilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-room-facilities/:id : get the "id" instanceRoomFacility.
     *
     * @param id the id of the instanceRoomFacility to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceRoomFacility, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-room-facilities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomFacility> getInstanceRoomFacility(@PathVariable Long id) {
        log.debug("REST request to get InstanceRoomFacility : {}", id);
        InstanceRoomFacility instanceRoomFacility = instanceRoomFacilityRepository.findOne(id);
        return Optional.ofNullable(instanceRoomFacility)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-room-facilities/:id : delete the "id" instanceRoomFacility.
     *
     * @param id the id of the instanceRoomFacility to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-room-facilities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceRoomFacility(@PathVariable Long id) {
        log.debug("REST request to delete InstanceRoomFacility : {}", id);
        instanceRoomFacilityRepository.delete(id);
        instanceRoomFacilitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceRoomFacility", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-room-facilities?query=:query : search for the instanceRoomFacility corresponding
     * to the query.
     *
     * @param query the query of the instanceRoomFacility search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-room-facilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceRoomFacility>> searchInstanceRoomFacilities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstanceRoomFacilities for query {}", query);
        Page<InstanceRoomFacility> page = instanceRoomFacilitySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-room-facilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
