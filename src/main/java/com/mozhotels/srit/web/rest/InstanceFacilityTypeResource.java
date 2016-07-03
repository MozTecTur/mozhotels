package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstanceFacilityType;
import com.mozhotels.srit.repository.InstanceFacilityTypeRepository;
import com.mozhotels.srit.repository.search.InstanceFacilityTypeSearchRepository;
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
 * REST controller for managing InstanceFacilityType.
 */
@RestController
@RequestMapping("/api")
public class InstanceFacilityTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstanceFacilityTypeResource.class);
        
    @Inject
    private InstanceFacilityTypeRepository instanceFacilityTypeRepository;
    
    @Inject
    private InstanceFacilityTypeSearchRepository instanceFacilityTypeSearchRepository;
    
    /**
     * POST  /instance-facility-types : Create a new instanceFacilityType.
     *
     * @param instanceFacilityType the instanceFacilityType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceFacilityType, or with status 400 (Bad Request) if the instanceFacilityType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-facility-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceFacilityType> createInstanceFacilityType(@Valid @RequestBody InstanceFacilityType instanceFacilityType) throws URISyntaxException {
        log.debug("REST request to save InstanceFacilityType : {}", instanceFacilityType);
        if (instanceFacilityType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceFacilityType", "idexists", "A new instanceFacilityType cannot already have an ID")).body(null);
        }
        InstanceFacilityType result = instanceFacilityTypeRepository.save(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-facility-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceFacilityType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-facility-types : Updates an existing instanceFacilityType.
     *
     * @param instanceFacilityType the instanceFacilityType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceFacilityType,
     * or with status 400 (Bad Request) if the instanceFacilityType is not valid,
     * or with status 500 (Internal Server Error) if the instanceFacilityType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-facility-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceFacilityType> updateInstanceFacilityType(@Valid @RequestBody InstanceFacilityType instanceFacilityType) throws URISyntaxException {
        log.debug("REST request to update InstanceFacilityType : {}", instanceFacilityType);
        if (instanceFacilityType.getId() == null) {
            return createInstanceFacilityType(instanceFacilityType);
        }
        InstanceFacilityType result = instanceFacilityTypeRepository.save(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceFacilityType", instanceFacilityType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-facility-types : get all the instanceFacilityTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instanceFacilityTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-facility-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceFacilityType>> getAllInstanceFacilityTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstanceFacilityTypes");
        Page<InstanceFacilityType> page = instanceFacilityTypeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-facility-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-facility-types/:id : get the "id" instanceFacilityType.
     *
     * @param id the id of the instanceFacilityType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceFacilityType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-facility-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceFacilityType> getInstanceFacilityType(@PathVariable Long id) {
        log.debug("REST request to get InstanceFacilityType : {}", id);
        InstanceFacilityType instanceFacilityType = instanceFacilityTypeRepository.findOne(id);
        return Optional.ofNullable(instanceFacilityType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-facility-types/:id : delete the "id" instanceFacilityType.
     *
     * @param id the id of the instanceFacilityType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-facility-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceFacilityType(@PathVariable Long id) {
        log.debug("REST request to delete InstanceFacilityType : {}", id);
        instanceFacilityTypeRepository.delete(id);
        instanceFacilityTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceFacilityType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-facility-types?query=:query : search for the instanceFacilityType corresponding
     * to the query.
     *
     * @param query the query of the instanceFacilityType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-facility-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceFacilityType>> searchInstanceFacilityTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstanceFacilityTypes for query {}", query);
        Page<InstanceFacilityType> page = instanceFacilityTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-facility-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
