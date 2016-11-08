package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Estudios;

import com.mycompany.myapp.repository.EstudiosRepository;
import com.mycompany.myapp.repository.search.EstudiosSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Estudios.
 */
@RestController
@RequestMapping("/api")
public class EstudiosResource {

    private final Logger log = LoggerFactory.getLogger(EstudiosResource.class);

    @Inject
    private EstudiosRepository estudiosRepository;

    @Inject
    private EstudiosSearchRepository estudiosSearchRepository;

    /**
     * POST  /estudios : Create a new estudios.
     *
     * @param estudios the estudios to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estudios, or with status 400 (Bad Request) if the estudios has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/estudios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estudios> createEstudios(@Valid @RequestBody Estudios estudios) throws URISyntaxException {
        log.debug("REST request to save Estudios : {}", estudios);
        if (estudios.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("estudios", "idexists", "A new estudios cannot already have an ID")).body(null);
        }
        Estudios result = estudiosRepository.save(estudios);
        estudiosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/estudios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("estudios", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estudios : Updates an existing estudios.
     *
     * @param estudios the estudios to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estudios,
     * or with status 400 (Bad Request) if the estudios is not valid,
     * or with status 500 (Internal Server Error) if the estudios couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/estudios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estudios> updateEstudios(@Valid @RequestBody Estudios estudios) throws URISyntaxException {
        log.debug("REST request to update Estudios : {}", estudios);
        if (estudios.getId() == null) {
            return createEstudios(estudios);
        }
        Estudios result = estudiosRepository.save(estudios);
        estudiosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("estudios", estudios.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estudios : get all the estudios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of estudios in body
     */
    @RequestMapping(value = "/estudios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estudios> getAllEstudios() {
        log.debug("REST request to get all Estudios");
        List<Estudios> estudios = estudiosRepository.findAll();
        return estudios;
    }

    /**
     * GET  /estudios/:id : get the "id" estudios.
     *
     * @param id the id of the estudios to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estudios, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/estudios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estudios> getEstudios(@PathVariable Long id) {
        log.debug("REST request to get Estudios : {}", id);
        Estudios estudios = estudiosRepository.findOne(id);
        return Optional.ofNullable(estudios)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estudios/:id : delete the "id" estudios.
     *
     * @param id the id of the estudios to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/estudios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEstudios(@PathVariable Long id) {
        log.debug("REST request to delete Estudios : {}", id);
        estudiosRepository.delete(id);
        estudiosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estudios", id.toString())).build();
    }

    /**
     * SEARCH  /_search/estudios?query=:query : search for the estudios corresponding
     * to the query.
     *
     * @param query the query of the estudios search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/estudios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estudios> searchEstudios(@RequestParam String query) {
        log.debug("REST request to search Estudios for query {}", query);
        return StreamSupport
            .stream(estudiosSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


    /**
     * GET  /estudios usuario.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of estudios in body
     */
    @RequestMapping(value = "/estudios/user/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estudios> getEstudiosUser(@PathVariable String login) {
        log.debug("REST request to get User Estudios");
        List<Estudios> estudios = estudiosRepository.findByUserEstudio(login);
        return estudios;
    }


}
