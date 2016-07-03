package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.InstanceTurType;
import com.mozhotels.srit.repository.InstanceTurTypeRepository;
import com.mozhotels.srit.repository.search.InstanceTurTypeSearchRepository;
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
 * REST controller for managing InstanceTurType.
 */
@RestController
@RequestMapping("/api")
public class InstanceTurTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstanceTurTypeResource.class);
        
    @Inject
    private InstanceTurTypeRepository instanceTurTypeRepository;
    
    @Inject
    private InstanceTurTypeSearchRepository instanceTurTypeSearchRepository;
    
    /**
     * POST  /instance-tur-types : Create a new instanceTurType.
     *
     * @param instanceTurType the instanceTurType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceTurType, or with status 400 (Bad Request) if the instanceTurType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-tur-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTurType> createInstanceTurType(@Valid @RequestBody InstanceTurType instanceTurType) throws URISyntaxException {
        log.debug("REST request to save InstanceTurType : {}", instanceTurType);
        if (instanceTurType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceTurType", "idexists", "A new instanceTurType cannot already have an ID")).body(null);
        }
        InstanceTurType result = instanceTurTypeRepository.save(instanceTurType);
        instanceTurTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-tur-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceTurType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-tur-types : Updates an existing instanceTurType.
     *
     * @param instanceTurType the instanceTurType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceTurType,
     * or with status 400 (Bad Request) if the instanceTurType is not valid,
     * or with status 500 (Internal Server Error) if the instanceTurType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-tur-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTurType> updateInstanceTurType(@Valid @RequestBody InstanceTurType instanceTurType) throws URISyntaxException {
        log.debug("REST request to update InstanceTurType : {}", instanceTurType);
        if (instanceTurType.getId() == null) {
            return createInstanceTurType(instanceTurType);
        }
        InstanceTurType result = instanceTurTypeRepository.save(instanceTurType);
        instanceTurTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceTurType", instanceTurType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-tur-types : get all the instanceTurTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of instanceTurTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/instance-tur-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceTurType>> getAllInstanceTurTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstanceTurTypes");
        Page<InstanceTurType> page = instanceTurTypeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/instance-tur-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /instance-tur-types/:id : get the "id" instanceTurType.
     *
     * @param id the id of the instanceTurType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceTurType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-tur-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTurType> getInstanceTurType(@PathVariable Long id) {
        log.debug("REST request to get InstanceTurType : {}", id);
        InstanceTurType instanceTurType = instanceTurTypeRepository.findOne(id);
        return Optional.ofNullable(instanceTurType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-tur-types/:id : delete the "id" instanceTurType.
     *
     * @param id the id of the instanceTurType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-tur-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceTurType(@PathVariable Long id) {
        log.debug("REST request to delete InstanceTurType : {}", id);
        instanceTurTypeRepository.delete(id);
        instanceTurTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceTurType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-tur-types?query=:query : search for the instanceTurType corresponding
     * to the query.
     *
     * @param query the query of the instanceTurType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-tur-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstanceTurType>> searchInstanceTurTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstanceTurTypes for query {}", query);
        Page<InstanceTurType> page = instanceTurTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/instance-tur-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
