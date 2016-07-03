package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstanceRoomFacilityType;
import com.mozhotels.srit.repository.InstanceRoomFacilityTypeRepository;
import com.mozhotels.srit.repository.search.InstanceRoomFacilityTypeSearchRepository;
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
 * REST controller for managing InstanceRoomFacilityType.
 */
@RestController
@RequestMapping("/api")
public class InstanceRoomFacilityTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstanceRoomFacilityTypeResource.class);
        
    @Inject
    private InstanceRoomFacilityTypeRepository instanceRoomFacilityTypeRepository;
    
    @Inject
    private InstanceRoomFacilityTypeSearchRepository instanceRoomFacilityTypeSearchRepository;
    
    /**
     * POST  /instance-room-facility-types : Create a new instanceRoomFacilityType.
     *
     * @param instanceRoomFacilityType the instanceRoomFacilityType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceRoomFacilityType, or with status 400 (Bad Request) if the instanceRoomFacilityType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-room-facility-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomFacilityType> createInstanceRoomFacilityType(@Valid @RequestBody InstanceRoomFacilityType instanceRoomFacilityType) throws URISyntaxException {
        log.debug("REST request to save InstanceRoomFacilityType : {}", instanceRoomFacilityType);
        if (instanceRoomFacilityType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceRoomFacilityType", "idexists", "A new instanceRoomFacilityType cannot already have an ID")).body(null);
        }
        InstanceRoomFacilityType result = instanceRoomFacilityTypeRepository.save(instanceRoomFacilityType);
        instanceRoomFacilityTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-room-facility-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceRoomFacilityType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-room-facility-types : Updates an existing instanceRoomFacilityType.
     *
     * @param instanceRoomFacilityType the instanceRoomFacilityType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceRoomFacilityType,
     * or with status 400 (Bad Request) if the instanceRoomFacilityType is not valid,
     * or with status 500 (Internal Server Error) if the instanceRoomFacilityType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-room-facility-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomFacilityType> updateInstanceRoomFacilityType(@Valid @RequestBody InstanceRoomFacilityType instanceRoomFacilityType) throws URISyntaxException {
        log.debug("REST request to update InstanceRoomFacilityType : {}", instanceRoomFacilityType);
        if (instanceRoomFacilityType.getId() == null) {
            return createInstanceRoomFacilityType(instanceRoomFacilityType);
        }
        InstanceRoomFacilityType result = instanceRoomFacilityTypeRepository.save(instanceRoomFacilityType);
        instanceRoomFacilityTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceRoomFacilityType", instanceRoomFacilityType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-room-facility-types : get all the instanceRoomFacilityTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instanceRoomFacilityTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-room-facility-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceRoomFacilityType>> getAllInstanceRoomFacilityTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstanceRoomFacilityTypes");
        Page<InstanceRoomFacilityType> page = instanceRoomFacilityTypeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-room-facility-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-room-facility-types/:id : get the "id" instanceRoomFacilityType.
     *
     * @param id the id of the instanceRoomFacilityType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceRoomFacilityType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-room-facility-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomFacilityType> getInstanceRoomFacilityType(@PathVariable Long id) {
        log.debug("REST request to get InstanceRoomFacilityType : {}", id);
        InstanceRoomFacilityType instanceRoomFacilityType = instanceRoomFacilityTypeRepository.findOne(id);
        return Optional.ofNullable(instanceRoomFacilityType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-room-facility-types/:id : delete the "id" instanceRoomFacilityType.
     *
     * @param id the id of the instanceRoomFacilityType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-room-facility-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceRoomFacilityType(@PathVariable Long id) {
        log.debug("REST request to delete InstanceRoomFacilityType : {}", id);
        instanceRoomFacilityTypeRepository.delete(id);
        instanceRoomFacilityTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceRoomFacilityType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-room-facility-types?query=:query : search for the instanceRoomFacilityType corresponding
     * to the query.
     *
     * @param query the query of the instanceRoomFacilityType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-room-facility-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceRoomFacilityType>> searchInstanceRoomFacilityTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstanceRoomFacilityTypes for query {}", query);
        Page<InstanceRoomFacilityType> page = instanceRoomFacilityTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-room-facility-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
