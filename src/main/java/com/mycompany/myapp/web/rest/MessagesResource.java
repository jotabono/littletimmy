package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Messages;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MessagesRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.MessagesSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Messages.
 */
@RestController
@RequestMapping("/api")
public class MessagesResource {

    private final Logger log = LoggerFactory.getLogger(MessagesResource.class);

    @Inject
    private MessagesRepository messagesRepository;

    @Inject
    private MessagesSearchRepository messagesSearchRepository;

    @Inject
    private UserRepository userRepository;
    /**
     * POST  /messages : Create a new messages.
     *
     * @param messages the messages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new messages, or with status 400 (Bad Request) if the messages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messages> createMessages(@RequestBody Messages messages) throws URISyntaxException {
        log.debug("REST request to save Messages : {}", messages);
        if (messages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("messages", "idexists", "A new messages cannot already have an ID")).body(null);
        }

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        messages.setSender(user);
        ZonedDateTime now = ZonedDateTime.now();
        messages.setSendDate(now);
        Messages result = messagesRepository.save(messages);
        messagesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("messages", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messages : Updates an existing messages.
     *
     * @param messages the messages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated messages,
     * or with status 400 (Bad Request) if the messages is not valid,
     * or with status 500 (Internal Server Error) if the messages couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messages> updateMessages(@RequestBody Messages messages) throws URISyntaxException {
        log.debug("REST request to update Messages : {}", messages);
        if (messages.getId() == null) {
            return createMessages(messages);
        }
        Messages result = messagesRepository.save(messages);
        messagesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("messages", messages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messages : get all the messages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of messages in body
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Messages> getAllMessages() {
        log.debug("REST request to get all Messages");
        List<Messages> messages = messagesRepository.findAll();
        return messages;
    }

    /**
     * GET  /messages/:id : get the "id" messages.
     *
     * @param id the id of the messages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the messages, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/messages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messages> getMessages(@PathVariable Long id) {
        log.debug("REST request to get Messages : {}", id);
        Messages messages = messagesRepository.findOne(id);
        return Optional.ofNullable(messages)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /messages/:id : delete the "id" messages.
     *
     * @param id the id of the messages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/messages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMessages(@PathVariable Long id) {
        log.debug("REST request to delete Messages : {}", id);
        messagesRepository.delete(id);
        messagesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("messages", id.toString())).build();
    }

    /**
     * SEARCH  /_search/messages?query=:query : search for the messages corresponding
     * to the query.
     *
     * @param query the query of the messages search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/messages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Messages> searchMessages(@RequestParam String query) {
        log.debug("REST request to search Messages for query {}", query);
        return StreamSupport
            .stream(messagesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
