package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Recomendacion;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.RecomendacionRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.RecomendacionSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
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
 * REST controller for managing Recomendacion.
 */
@RestController
@RequestMapping("/api")
public class RecomendacionResource {

    private final Logger log = LoggerFactory.getLogger(RecomendacionResource.class);

    @Inject
    private RecomendacionRepository recomendacionRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RecomendacionSearchRepository recomendacionSearchRepository;

    /**
     * POST  /recomendacions : Create a new recomendacion.
     *
     * @param recomendacion the recomendacion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recomendacion, or with status 400 (Bad Request) if the recomendacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/recomendacions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recomendacion> createRecomendacion(@Valid @RequestBody Recomendacion recomendacion) throws URISyntaxException {
        log.debug("REST request to save Recomendacion : {}", recomendacion);
        if (recomendacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("recomendacion", "idexists", "A new recomendacion cannot already have an ID")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        recomendacion.setRecomendador(user);
        recomendacion.setAceptada(recomendacion.isAceptada());
        recomendacion.setEmpresa(recomendacion.getTrabajo().getEmpresa());
        Recomendacion result = recomendacionRepository.save(recomendacion);
        recomendacionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/recomendacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recomendacion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recomendacions : Updates an existing recomendacion.
     *
     * @param recomendacion the recomendacion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recomendacion,
     * or with status 400 (Bad Request) if the recomendacion is not valid,
     * or with status 500 (Internal Server Error) if the recomendacion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/recomendacions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recomendacion> updateRecomendacion(@Valid @RequestBody Recomendacion recomendacion) throws URISyntaxException {
        log.debug("REST request to update Recomendacion : {}", recomendacion);
        if (recomendacion.getId() == null) {
            return createRecomendacion(recomendacion);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        recomendacion.setRecomendador(user);
        recomendacion.setAceptada(recomendacion.isAceptada());
        recomendacion.setEmpresa(recomendacion.getTrabajo().getEmpresa());
        Recomendacion result = recomendacionRepository.save(recomendacion);
        recomendacionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recomendacion", recomendacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recomendacions : get all the recomendacions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of recomendacions in body
     */
    @RequestMapping(value = "/recomendacions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Recomendacion> getAllRecomendacions() {
        log.debug("REST request to get all Recomendacions");
        List<Recomendacion> recomendacions = recomendacionRepository.findAll();
        return recomendacions;
    }

    /**
     * GET  /recomendacions/:id : get the "id" recomendacion.
     *
     * @param id the id of the recomendacion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recomendacion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/recomendacions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recomendacion> getRecomendacion(@PathVariable Long id) {
        log.debug("REST request to get Recomendacion : {}", id);
        Recomendacion recomendacion = recomendacionRepository.findOne(id);
        return Optional.ofNullable(recomendacion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /recomendacions/:id : delete the "id" recomendacion.
     *
     * @param id the id of the recomendacion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/recomendacions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecomendacion(@PathVariable Long id) {
        log.debug("REST request to delete Recomendacion : {}", id);
        recomendacionRepository.delete(id);
        recomendacionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recomendacion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/recomendacions?query=:query : search for the recomendacion corresponding
     * to the query.
     *
     * @param query the query of the recomendacion search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/recomendacions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Recomendacion> searchRecomendacions(@RequestParam String query) {
        log.debug("REST request to search Recomendacions for query {}", query);
        return StreamSupport
            .stream(recomendacionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
