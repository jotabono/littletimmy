package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Centro;

import com.mycompany.myapp.repository.CentroRepository;
import com.mycompany.myapp.repository.search.CentroSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Centro.
 */
@RestController
@RequestMapping("/api")
public class CentroResource {

    private final Logger log = LoggerFactory.getLogger(CentroResource.class);
        
    @Inject
    private CentroRepository centroRepository;

    @Inject
    private CentroSearchRepository centroSearchRepository;

    /**
     * POST  /centros : Create a new centro.
     *
     * @param centro the centro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new centro, or with status 400 (Bad Request) if the centro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/centros",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Centro> createCentro(@RequestBody Centro centro) throws URISyntaxException {
        log.debug("REST request to save Centro : {}", centro);
        if (centro.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("centro", "idexists", "A new centro cannot already have an ID")).body(null);
        }
        Centro result = centroRepository.save(centro);
        centroSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/centros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("centro", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /centros : Updates an existing centro.
     *
     * @param centro the centro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated centro,
     * or with status 400 (Bad Request) if the centro is not valid,
     * or with status 500 (Internal Server Error) if the centro couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/centros",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Centro> updateCentro(@RequestBody Centro centro) throws URISyntaxException {
        log.debug("REST request to update Centro : {}", centro);
        if (centro.getId() == null) {
            return createCentro(centro);
        }
        Centro result = centroRepository.save(centro);
        centroSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("centro", centro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /centros : get all the centros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of centros in body
     */
    @RequestMapping(value = "/centros",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Centro> getAllCentros() {
        log.debug("REST request to get all Centros");
        List<Centro> centros = centroRepository.findAll();
        return centros;
    }

    /**
     * GET  /centros/:id : get the "id" centro.
     *
     * @param id the id of the centro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the centro, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/centros/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Centro> getCentro(@PathVariable Long id) {
        log.debug("REST request to get Centro : {}", id);
        Centro centro = centroRepository.findOne(id);
        return Optional.ofNullable(centro)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /centros/:id : delete the "id" centro.
     *
     * @param id the id of the centro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/centros/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCentro(@PathVariable Long id) {
        log.debug("REST request to delete Centro : {}", id);
        centroRepository.delete(id);
        centroSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("centro", id.toString())).build();
    }

    /**
     * SEARCH  /_search/centros?query=:query : search for the centro corresponding
     * to the query.
     *
     * @param query the query of the centro search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/centros",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Centro> searchCentros(@RequestParam String query) {
        log.debug("REST request to search Centros for query {}", query);
        return StreamSupport
            .stream(centroSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
