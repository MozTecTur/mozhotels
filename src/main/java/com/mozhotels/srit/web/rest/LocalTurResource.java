package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.LocalTur;
import com.mozhotels.srit.repository.LocalTurRepository;
import com.mozhotels.srit.repository.search.LocalTurSearchRepository;
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
 * REST controller for managing LocalTur.
 */
@RestController
@RequestMapping("/api")
public class LocalTurResource {

    private final Logger log = LoggerFactory.getLogger(LocalTurResource.class);
        
    @Inject
    private LocalTurRepository localTurRepository;
    
    @Inject
    private LocalTurSearchRepository localTurSearchRepository;
    
    /**
     * POST  /local-turs : Create a new localTur.
     *
     * @param localTur the localTur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new localTur, or with status 400 (Bad Request) if the localTur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/local-turs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocalTur> createLocalTur(@Valid @RequestBody LocalTur localTur) throws URISyntaxException {
        log.debug("REST request to save LocalTur : {}", localTur);
        if (localTur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("localTur", "idexists", "A new localTur cannot already have an ID")).body(null);
        }
        LocalTur result = localTurRepository.save(localTur);
        localTurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/local-turs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("localTur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /local-turs : Updates an existing localTur.
     *
     * @param localTur the localTur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated localTur,
     * or with status 400 (Bad Request) if the localTur is not valid,
     * or with status 500 (Internal Server Error) if the localTur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/local-turs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocalTur> updateLocalTur(@Valid @RequestBody LocalTur localTur) throws URISyntaxException {
        log.debug("REST request to update LocalTur : {}", localTur);
        if (localTur.getId() == null) {
            return createLocalTur(localTur);
        }
        LocalTur result = localTurRepository.save(localTur);
        localTurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("localTur", localTur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /local-turs : get all the localTurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of localTurs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/local-turs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LocalTur>> getAllLocalTurs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LocalTurs");
        Page<LocalTur> page = localTurRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/local-turs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /local-turs/:id : get the "id" localTur.
     *
     * @param id the id of the localTur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the localTur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/local-turs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocalTur> getLocalTur(@PathVariable Long id) {
        log.debug("REST request to get LocalTur : {}", id);
        LocalTur localTur = localTurRepository.findOne(id);
        return Optional.ofNullable(localTur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /local-turs/:id : delete the "id" localTur.
     *
     * @param id the id of the localTur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/local-turs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLocalTur(@PathVariable Long id) {
        log.debug("REST request to delete LocalTur : {}", id);
        localTurRepository.delete(id);
        localTurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("localTur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/local-turs?query=:query : search for the localTur corresponding
     * to the query.
     *
     * @param query the query of the localTur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/local-turs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LocalTur>> searchLocalTurs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LocalTurs for query {}", query);
        Page<LocalTur> page = localTurSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/local-turs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
