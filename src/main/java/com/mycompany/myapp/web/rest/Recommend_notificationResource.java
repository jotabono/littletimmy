package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Recommend_notification;

import com.mycompany.myapp.repository.Recommend_notificationRepository;
import com.mycompany.myapp.repository.search.Recommend_notificationSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Recommend_notification.
 */
@RestController
@RequestMapping("/api")
public class Recommend_notificationResource {

    private final Logger log = LoggerFactory.getLogger(Recommend_notificationResource.class);

    @Inject
    private Recommend_notificationRepository recommend_notificationRepository;

    @Inject
    private Recommend_notificationSearchRepository recommend_notificationSearchRepository;

    /**
     * POST  /recommend-notifications : Create a new recommend_notification.
     *
     * @param recommend_notification the recommend_notification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recommend_notification, or with status 400 (Bad Request) if the recommend_notification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/recommend-notifications",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recommend_notification> createRecommend_notification(@RequestBody Recommend_notification recommend_notification) throws URISyntaxException {
        log.debug("REST request to save Recommend_notification : {}", recommend_notification);
        if (recommend_notification.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("recommend_notification", "idexists", "A new recommend_notification cannot already have an ID")).body(null);
        }
        recommend_notification.setLeida(false);
        Recommend_notification result = recommend_notificationRepository.save(recommend_notification);
        recommend_notificationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/recommend-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recommend_notification", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recommend-notifications : Updates an existing recommend_notification.
     *
     * @param recommend_notification the recommend_notification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recommend_notification,
     * or with status 400 (Bad Request) if the recommend_notification is not valid,
     * or with status 500 (Internal Server Error) if the recommend_notification couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/recommend-notifications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recommend_notification> updateRecommend_notification(@RequestBody Recommend_notification recommend_notification) throws URISyntaxException {
        log.debug("REST request to update Recommend_notification : {}", recommend_notification);
        if (recommend_notification.getId() == null) {
            return createRecommend_notification(recommend_notification);
        }
        Recommend_notification result = recommend_notificationRepository.save(recommend_notification);
        recommend_notificationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recommend_notification", recommend_notification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recommend-notifications : get all the recommend_notifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of recommend_notifications in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/recommend-notifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Recommend_notification>> getAllRecommend_notifications(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Recommend_notifications");
        Page<Recommend_notification> page = recommend_notificationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/recommend-notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /recommend-notifications/:id : get the "id" recommend_notification.
     *
     * @param id the id of the recommend_notification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recommend_notification, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/recommend-notifications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recommend_notification> getRecommend_notification(@PathVariable Long id) {
        log.debug("REST request to get Recommend_notification : {}", id);
        Recommend_notification recommend_notification = recommend_notificationRepository.findOne(id);
        return Optional.ofNullable(recommend_notification)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /recommend-notifications/:id : delete the "id" recommend_notification.
     *
     * @param id the id of the recommend_notification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/recommend-notifications/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecommend_notification(@PathVariable Long id) {
        log.debug("REST request to delete Recommend_notification : {}", id);
        recommend_notificationRepository.delete(id);
        recommend_notificationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recommend_notification", id.toString())).build();
    }

    /**
     * SEARCH  /_search/recommend-notifications?query=:query : search for the recommend_notification corresponding
     * to the query.
     *
     * @param query the query of the recommend_notification search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/recommend-notifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Recommend_notification>> searchRecommend_notifications(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Recommend_notifications for query {}", query);
        Page<Recommend_notification> page = recommend_notificationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/recommend-notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/recommend-notifications/user-conected",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Recommend_notification>> getRecommendNotificationsNotReaded()
        throws URISyntaxException {
        log.debug("REST request to get a page of Recommend_notifications");

        List<Recommend_notification> notifications = recommend_notificationRepository.findByRemitenteIsCurrentUser();

        return new ResponseEntity<List<Recommend_notification>>(notifications, HttpStatus.OK);

    }


}
