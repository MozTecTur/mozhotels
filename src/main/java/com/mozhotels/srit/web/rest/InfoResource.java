package com.mozhotels.srit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mozhotels.srit.domain.Info;
import com.mozhotels.srit.repository.InfoRepository;
import com.mozhotels.srit.repository.search.InfoSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Info.
 */
@RestController
@RequestMapping("/api")
public class InfoResource {

    private final Logger log = LoggerFactory.getLogger(InfoResource.class);
        
    @Inject
    private InfoRepository infoRepository;
    
    @Inject
    private InfoSearchRepository infoSearchRepository;
    
    /**
     * POST  /infos : Create a new info.
     *
     * @param info the info to create
     * @return the ResponseEntity with status 201 (Created) and with body the new info, or with status 400 (Bad Request) if the info has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Info> createInfo(@RequestBody Info info) throws URISyntaxException {
        log.debug("REST request to save Info : {}", info);
        if (info.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("info", "idexists", "A new info cannot already have an ID")).body(null);
        }
        Info result = infoRepository.save(info);
        infoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("info", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /infos : Updates an existing info.
     *
     * @param info the info to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated info,
     * or with status 400 (Bad Request) if the info is not valid,
     * or with status 500 (Internal Server Error) if the info couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Info> updateInfo(@RequestBody Info info) throws URISyntaxException {
        log.debug("REST request to update Info : {}", info);
        if (info.getId() == null) {
            return createInfo(info);
        }
        Info result = infoRepository.save(info);
        infoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("info", info.getId().toString()))
            .body(result);
    }

    /**
     * GET  /infos : get all the infos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of infos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Info>> getAllInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Infos");
        Page<Info> page = infoRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /infos/:id : get the "id" info.
     *
     * @param id the id of the info to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the info, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Info> getInfo(@PathVariable Long id) {
        log.debug("REST request to get Info : {}", id);
        Info info = infoRepository.findOne(id);
        return Optional.ofNullable(info)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /infos/:id : delete the "id" info.
     *
     * @param id the id of the info to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInfo(@PathVariable Long id) {
        log.debug("REST request to delete Info : {}", id);
        infoRepository.delete(id);
        infoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("info", id.toString())).build();
    }

    /**
     * SEARCH  /_search/infos?query=:query : search for the info corresponding
     * to the query.
     *
     * @param query the query of the info search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Info>> searchInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Infos for query {}", query);
        Page<Info> page = infoSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
