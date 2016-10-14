package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Recomendacion;
import com.mycompany.myapp.domain.Trabajo;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.TrabajoRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.TrabajoSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.RecomendacionService;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Trabajo.
 */
@RestController
@RequestMapping("/api")
public class TrabajoResource {

    private final Logger log = LoggerFactory.getLogger(TrabajoResource.class);

    @Inject
    private TrabajoRepository trabajoRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TrabajoSearchRepository trabajoSearchRepository;

    @Inject
    private RecomendacionService recomendacionService;

    /**
     * POST  /trabajos : Create a new trabajo.
     *
     * @param trabajo the trabajo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trabajo, or with status 400 (Bad Request) if the trabajo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trabajos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajo> createTrabajo(@RequestBody Trabajo trabajo) throws URISyntaxException {
        log.debug("REST request to save Trabajo : {}", trabajo);
        if (trabajo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trabajo", "idexists", "A new trabajo cannot already have an ID")).body(null);
        }
        Trabajo result = trabajoRepository.save(trabajo);
        trabajoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/trabajos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trabajo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trabajos : Updates an existing trabajo.
     *
     * @param trabajo the trabajo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trabajo,
     * or with status 400 (Bad Request) if the trabajo is not valid,
     * or with status 500 (Internal Server Error) if the trabajo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trabajos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajo> updateTrabajo(@RequestBody Trabajo trabajo) throws URISyntaxException {
        log.debug("REST request to update Trabajo : {}", trabajo);
        if (trabajo.getId() == null) {
            return createTrabajo(trabajo);
        }
        Trabajo result = trabajoRepository.save(trabajo);
        trabajoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trabajo", trabajo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trabajos : get all the trabajos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trabajos in body
     */
    @RequestMapping(value = "/trabajos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trabajo> getAllTrabajos() {
        log.debug("REST request to get all Trabajos");
        List<Trabajo> trabajos = trabajoRepository.findAll();
        return trabajos;
    }

    /**
     * GET  /trabajos/:id : get the "id" trabajo.
     *
     * @param id the id of the trabajo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trabajo, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/trabajos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajo> getTrabajo(@PathVariable Long id) {
        log.debug("REST request to get Trabajo : {}", id);
        Trabajo trabajo = trabajoRepository.findOne(id);
        return Optional.ofNullable(trabajo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trabajos/:id : delete the "id" trabajo.
     *
     * @param id the id of the trabajo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/trabajos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTrabajo(@PathVariable Long id) {
        log.debug("REST request to delete Trabajo : {}", id);
        trabajoRepository.delete(id);
        trabajoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trabajo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/trabajos?query=:query : search for the trabajo corresponding
     * to the query.
     *
     * @param query the query of the trabajo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/trabajos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trabajo> searchTrabajos(@RequestParam String query) {
        log.debug("REST request to search Trabajos for query {}", query);
        return StreamSupport
            .stream(trabajoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/trabajos/usuarios/{recomendador}/{recomendado}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Trabajo> getAllTrabajosEntreUsers(@PathVariable String recomendador, @PathVariable String recomendado) {
        log.debug("REST request to get all Trabajos");
        //User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Set<Trabajo> trabajos = recomendacionService.getTrabajosUsuarios(recomendador, recomendado);
        return trabajos;
    }

    @RequestMapping(value = "/trabajos/user/{recomendado}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Trabajo> getAllTrabajosUser(@PathVariable String recomendado) {
        log.debug("REST request to get all Trabajos");
        //User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Set<Trabajo> trabajos = recomendacionService.getTrabajosComunesUsuarios(recomendado);
        return trabajos;
    }

}
