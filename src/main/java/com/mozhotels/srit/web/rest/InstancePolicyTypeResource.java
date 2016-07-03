package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstancePolicyType;
import com.mozhotels.srit.repository.InstancePolicyTypeRepository;
import com.mozhotels.srit.repository.search.InstancePolicyTypeSearchRepository;
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
 * REST controller for managing InstancePolicyType.
 */
@RestController
@RequestMapping("/api")
public class InstancePolicyTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstancePolicyTypeResource.class);
        
    @Inject
    private InstancePolicyTypeRepository instancePolicyTypeRepository;
    
    @Inject
    private InstancePolicyTypeSearchRepository instancePolicyTypeSearchRepository;
    
    /**
     * POST  /instance-policy-types : Create a new instancePolicyType.
     *
     * @param instancePolicyType the instancePolicyType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instancePolicyType, or with status 400 (Bad Request) if the instancePolicyType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-policy-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstancePolicyType> createInstancePolicyType(@Valid @RequestBody InstancePolicyType instancePolicyType) throws URISyntaxException {
        log.debug("REST request to save InstancePolicyType : {}", instancePolicyType);
        if (instancePolicyType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instancePolicyType", "idexists", "A new instancePolicyType cannot already have an ID")).body(null);
        }
        InstancePolicyType result = instancePolicyTypeRepository.save(instancePolicyType);
        instancePolicyTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-policy-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instancePolicyType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-policy-types : Updates an existing instancePolicyType.
     *
     * @param instancePolicyType the instancePolicyType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instancePolicyType,
     * or with status 400 (Bad Request) if the instancePolicyType is not valid,
     * or with status 500 (Internal Server Error) if the instancePolicyType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-policy-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstancePolicyType> updateInstancePolicyType(@Valid @RequestBody InstancePolicyType instancePolicyType) throws URISyntaxException {
        log.debug("REST request to update InstancePolicyType : {}", instancePolicyType);
        if (instancePolicyType.getId() == null) {
            return createInstancePolicyType(instancePolicyType);
        }
        InstancePolicyType result = instancePolicyTypeRepository.save(instancePolicyType);
        instancePolicyTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instancePolicyType", instancePolicyType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-policy-types : get all the instancePolicyTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instancePolicyTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-policy-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstancePolicyType>> getAllInstancePolicyTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstancePolicyTypes");
        Page<InstancePolicyType> page = instancePolicyTypeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-policy-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-policy-types/:id : get the "id" instancePolicyType.
     *
     * @param id the id of the instancePolicyType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instancePolicyType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-policy-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstancePolicyType> getInstancePolicyType(@PathVariable Long id) {
        log.debug("REST request to get InstancePolicyType : {}", id);
        InstancePolicyType instancePolicyType = instancePolicyTypeRepository.findOne(id);
        return Optional.ofNullable(instancePolicyType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-policy-types/:id : delete the "id" instancePolicyType.
     *
     * @param id the id of the instancePolicyType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-policy-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstancePolicyType(@PathVariable Long id) {
        log.debug("REST request to delete InstancePolicyType : {}", id);
        instancePolicyTypeRepository.delete(id);
        instancePolicyTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instancePolicyType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-policy-types?query=:query : search for the instancePolicyType corresponding
     * to the query.
     *
     * @param query the query of the instancePolicyType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-policy-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstancePolicyType>> searchInstancePolicyTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstancePolicyTypes for query {}", query);
        Page<InstancePolicyType> page = instancePolicyTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-policy-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
