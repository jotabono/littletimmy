package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Friendship_notification;

import com.mycompany.myapp.repository.Friendship_notificationRepository;
import com.mycompany.myapp.repository.search.Friendship_notificationSearchRepository;
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
 * REST controller for managing Friendship_notification.
 */
@RestController
@RequestMapping("/api")
public class Friendship_notificationResource {

    private final Logger log = LoggerFactory.getLogger(Friendship_notificationResource.class);
        
    @Inject
    private Friendship_notificationRepository friendship_notificationRepository;

    @Inject
    private Friendship_notificationSearchRepository friendship_notificationSearchRepository;

    /**
     * POST  /friendship-notifications : Create a new friendship_notification.
     *
     * @param friendship_notification the friendship_notification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new friendship_notification, or with status 400 (Bad Request) if the friendship_notification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/friendship-notifications",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Friendship_notification> createFriendship_notification(@RequestBody Friendship_notification friendship_notification) throws URISyntaxException {
        log.debug("REST request to save Friendship_notification : {}", friendship_notification);
        if (friendship_notification.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("friendship_notification", "idexists", "A new friendship_notification cannot already have an ID")).body(null);
        }
        Friendship_notification result = friendship_notificationRepository.save(friendship_notification);
        friendship_notificationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/friendship-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("friendship_notification", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /friendship-notifications : Updates an existing friendship_notification.
     *
     * @param friendship_notification the friendship_notification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated friendship_notification,
     * or with status 400 (Bad Request) if the friendship_notification is not valid,
     * or with status 500 (Internal Server Error) if the friendship_notification couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/friendship-notifications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Friendship_notification> updateFriendship_notification(@RequestBody Friendship_notification friendship_notification) throws URISyntaxException {
        log.debug("REST request to update Friendship_notification : {}", friendship_notification);
        if (friendship_notification.getId() == null) {
            return createFriendship_notification(friendship_notification);
        }
        Friendship_notification result = friendship_notificationRepository.save(friendship_notification);
        friendship_notificationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("friendship_notification", friendship_notification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /friendship-notifications : get all the friendship_notifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of friendship_notifications in body
     */
    @RequestMapping(value = "/friendship-notifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Friendship_notification> getAllFriendship_notifications() {
        log.debug("REST request to get all Friendship_notifications");
        List<Friendship_notification> friendship_notifications = friendship_notificationRepository.findAll();
        return friendship_notifications;
    }

    /**
     * GET  /friendship-notifications/:id : get the "id" friendship_notification.
     *
     * @param id the id of the friendship_notification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the friendship_notification, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/friendship-notifications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Friendship_notification> getFriendship_notification(@PathVariable Long id) {
        log.debug("REST request to get Friendship_notification : {}", id);
        Friendship_notification friendship_notification = friendship_notificationRepository.findOne(id);
        return Optional.ofNullable(friendship_notification)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /friendship-notifications/:id : delete the "id" friendship_notification.
     *
     * @param id the id of the friendship_notification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/friendship-notifications/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFriendship_notification(@PathVariable Long id) {
        log.debug("REST request to delete Friendship_notification : {}", id);
        friendship_notificationRepository.delete(id);
        friendship_notificationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("friendship_notification", id.toString())).build();
    }

    /**
     * SEARCH  /_search/friendship-notifications?query=:query : search for the friendship_notification corresponding
     * to the query.
     *
     * @param query the query of the friendship_notification search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/friendship-notifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Friendship_notification> searchFriendship_notifications(@RequestParam String query) {
        log.debug("REST request to search Friendship_notifications for query {}", query);
        return StreamSupport
            .stream(friendship_notificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
