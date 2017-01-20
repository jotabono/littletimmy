package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Chat;

import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ChatRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.ChatSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.ChatDTO;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Chat.
 */
@RestController
@RequestMapping("/api")
public class ChatResource {

    private final Logger log = LoggerFactory.getLogger(ChatResource.class);

    @Inject
    private ChatRepository chatRepository;

    @Inject
    private ChatSearchRepository chatSearchRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /chats : Create a new chat.
     *
     * @param chat the chat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chat, or with status 400 (Bad Request) if the chat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chat> createChat(@Valid @RequestBody Chat chat) throws URISyntaxException {
        log.debug("REST request to save Chat : {}", chat);
        if (chat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("chat", "idexists", "A new chat cannot already have an ID")).body(null);
        }

        if(chat.getName() == null){
            List<User> users = chat.getUsers().stream().collect(Collectors.toList());
            String user = users.get(0).getFirstName() + " " + users.get(0).getLastName();
            String chatName = "Conversation with " + user;
            chat.setName(chatName);
        }

        Chat result = chatRepository.save(chat);
        chatSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("chat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chats : Updates an existing chat.
     *
     * @param chat the chat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chat,
     * or with status 400 (Bad Request) if the chat is not valid,
     * or with status 500 (Internal Server Error) if the chat couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/chats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Chat> updateChat(@Valid @RequestBody Chat chat) throws URISyntaxException {
        log.debug("REST request to update Chat : {}", chat);
        if (chat.getId() == null) {
            return createChat(chat);
        }
        Chat result = chatRepository.save(chat);
        chatSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("chat", chat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chats : get all the chats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chats in body
     */
    @RequestMapping(value = "/chats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Chat> getAllChats() {
        log.debug("REST request to get all Chats");
        //List<Chat> chats = chatRepository.findAllWithEagerRelationships();

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        List<Chat> chats = chatRepository.findUserChats(user.getLogin());

        return chats;
    }

    /**
     * GET  /chats/:id : get the "id" chat.
     *
     * @param id the id of the chat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chat, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/chats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ChatDTO> getChat(@PathVariable Long id) {
        log.debug("REST request to get Chat : {}", id);
        Chat chat = chatRepository.findOneWithEagerRelationships(id);

        if(chat == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        if(!chat.getUsers().contains(user)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Messages> messages = chatRepository.findChatMessages(id);

        Collections.sort((List<Messages>) messages, (obj1, obj2) -> obj1.getSendDate().compareTo(obj2.getSendDate()));

        ChatDTO chatDTO = new ChatDTO(chat.getId(), chat.getName(), chat.getCreationDate(), chat.getOwner(), chat.getUsers(), messages);

        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    /**
     * DELETE  /chats/:id : delete the "id" chat.
     *
     * @param id the id of the chat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/chats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        log.debug("REST request to delete Chat : {}", id);
        chatRepository.delete(id);
        chatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("chat", id.toString())).build();
    }

    /**
     * SEARCH  /_search/chats?query=:query : search for the chat corresponding
     * to the query.
     *
     * @param query the query of the chat search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/chats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Chat> searchChats(@RequestParam String query) {
        log.debug("REST request to search Chats for query {}", query);
        return StreamSupport
            .stream(chatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
