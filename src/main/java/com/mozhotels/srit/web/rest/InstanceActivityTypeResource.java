package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstanceActivityType;
import com.mozhotels.srit.repository.InstanceActivityTypeRepository;
import com.mozhotels.srit.repository.search.InstanceActivityTypeSearchRepository;
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
 * REST controller for managing InstanceActivityType.
 */
@RestController
@RequestMapping("/api")
public class InstanceActivityTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstanceActivityTypeResource.class);
        
    @Inject
    private InstanceActivityTypeRepository instanceActivityTypeRepository;
    
    @Inject
    private InstanceActivityTypeSearchRepository instanceActivityTypeSearchRepository;
    
    /**
     * POST  /instance-activity-types : Create a new instanceActivityType.
     *
     * @param instanceActivityType the instanceActivityType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceActivityType, or with status 400 (Bad Request) if the instanceActivityType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-activity-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceActivityType> createInstanceActivityType(@Valid @RequestBody InstanceActivityType instanceActivityType) throws URISyntaxException {
        log.debug("REST request to save InstanceActivityType : {}", instanceActivityType);
        if (instanceActivityType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceActivityType", "idexists", "A new instanceActivityType cannot already have an ID")).body(null);
        }
        InstanceActivityType result = instanceActivityTypeRepository.save(instanceActivityType);
        instanceActivityTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-activity-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceActivityType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-activity-types : Updates an existing instanceActivityType.
     *
     * @param instanceActivityType the instanceActivityType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceActivityType,
     * or with status 400 (Bad Request) if the instanceActivityType is not valid,
     * or with status 500 (Internal Server Error) if the instanceActivityType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-activity-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceActivityType> updateInstanceActivityType(@Valid @RequestBody InstanceActivityType instanceActivityType) throws URISyntaxException {
        log.debug("REST request to update InstanceActivityType : {}", instanceActivityType);
        if (instanceActivityType.getId() == null) {
            return createInstanceActivityType(instanceActivityType);
        }
        InstanceActivityType result = instanceActivityTypeRepository.save(instanceActivityType);
        instanceActivityTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceActivityType", instanceActivityType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-activity-types : get all the instanceActivityTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instanceActivityTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-activity-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceActivityType>> getAllInstanceActivityTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstanceActivityTypes");
        Page<InstanceActivityType> page = instanceActivityTypeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-activity-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-activity-types/:id : get the "id" instanceActivityType.
     *
     * @param id the id of the instanceActivityType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceActivityType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-activity-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceActivityType> getInstanceActivityType(@PathVariable Long id) {
        log.debug("REST request to get InstanceActivityType : {}", id);
        InstanceActivityType instanceActivityType = instanceActivityTypeRepository.findOne(id);
        return Optional.ofNullable(instanceActivityType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-activity-types/:id : delete the "id" instanceActivityType.
     *
     * @param id the id of the instanceActivityType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-activity-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceActivityType(@PathVariable Long id) {
        log.debug("REST request to delete InstanceActivityType : {}", id);
        instanceActivityTypeRepository.delete(id);
        instanceActivityTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceActivityType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-activity-types?query=:query : search for the instanceActivityType corresponding
     * to the query.
     *
     * @param query the query of the instanceActivityType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-activity-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceActivityType>> searchInstanceActivityTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstanceActivityTypes for query {}", query);
        Page<InstanceActivityType> page = instanceActivityTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-activity-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
