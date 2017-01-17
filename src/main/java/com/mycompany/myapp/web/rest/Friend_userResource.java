package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Friend_user;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.Friend_userRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.Friend_userSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Friend_user.
 */
@RestController
@RequestMapping("/api")
public class Friend_userResource {

    private final Logger log = LoggerFactory.getLogger(Friend_userResource.class);

    @Inject
    private Friend_userRepository friend_userRepository;

    @Inject
    private Friend_userSearchRepository friend_userSearchRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /friend-users : Create a new friend_user.
     *
     * @param friend_user the friend_user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new friend_user, or with status 400 (Bad Request) if the friend_user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    @RequestMapping(value = "/friend-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Friend_user> createFriend_user(@Valid @RequestBody Friend_user friend_user) throws URISyntaxException {
        log.debug("REST request to save Friend_user : {}", friend_user);
        if (friend_user.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("friend_user", "idexists", "A new friend_user cannot already have an ID")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        friend_user.setFriend_from(user);
        Friend_user exist = friend_userRepository.findExistFriendship(friend_user.getFriend_from().getLogin(), friend_user.getFriend_to().getLogin());
        if(exist != null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("friend_user", "friendshipexist", "You and "+friend_user.getFriend_to().getLogin()+" are already friends")).body(null);
        }

        friend_user.setFriendship(false);
        Friend_user result = friend_userRepository.save(friend_user);
        friend_userSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/friend-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("friend_user", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /friend-users : Updates an existing friend_user.
     *
     * @param friend_user the friend_user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated friend_user,
     * or with status 400 (Bad Request) if the friend_user is not valid,
     * or with status 500 (Internal Server Error) if the friend_user couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/friend-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Friend_user> updateFriend_user(@Valid @RequestBody Friend_user friend_user) throws URISyntaxException {
        log.debug("REST request to update Friend_user : {}", friend_user);
        if (friend_user.getId() == null) {
            return createFriend_user(friend_user);
        }
        Friend_user result = friend_userRepository.save(friend_user);
        friend_userSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("friend_user", friend_user.getId().toString()))
            .body(result);
    }

    /**
     * GET  /friend-users : get all the friend_users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of friend_users in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/friend-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Friend_user>> getAllFriend_users(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Friend_users");
        Page<Friend_user> page = friend_userRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/friend-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /friend-users/:id : get the "id" friend_user.
     *
     * @param id the id of the friend_user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the friend_user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/friend-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Friend_user> getFriend_user(@PathVariable Long id) {
        log.debug("REST request to get Friend_user : {}", id);
        Friend_user friend_user = friend_userRepository.findOne(id);
        return Optional.ofNullable(friend_user)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /friend-users/:id : delete the "id" friend_user.
     *
     * @param id the id of the friend_user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/friend-users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFriend_user(@PathVariable Long id) {
        log.debug("REST request to delete Friend_user : {}", id);
        friend_userRepository.delete(id);
        friend_userSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("friend_user", id.toString())).build();
    }

    /**
     * SEARCH  /_search/friend-users?query=:query : search for the friend_user corresponding
     * to the query.
     *
     * @param query the query of the friend_user search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/friend-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Friend_user>> searchFriend_users(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Friend_users for query {}", query);
        Page<Friend_user> page = friend_userSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/friend-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{login}/friends",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<User>> getFriendsUser(@PathVariable String login) {
        log.debug("REST request to delete Friend_user : {}", login);

        Set<Friend_user> friendsUser = friend_userRepository.findFriendsOfUser(login);

        friendsUser = friendsUser.stream().filter(friend_user -> friend_user.isFriendship()).collect(Collectors.toSet());

        Set<User> friends = new HashSet<>();

        for(Friend_user friend_user:friendsUser){
            String from = friend_user.getFriend_from().getLogin();
            String to = friend_user.getFriend_to().getLogin();
            String sad = "das";
            if(from.equals(login)){
                friends.add(friend_user.getFriend_to());
            }
            else{
                friends.add(friend_user.getFriend_from());
            }
        }

        return new ResponseEntity<>(friends,HttpStatus.OK);
    }


    // Si son amigos

    @RequestMapping(value = "/friendship/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Friend_user> getFrienship(@PathVariable String login) {
        log.debug("REST request to delete Friend_user : {}", login);

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Friend_user friendsUser = friend_userRepository.findExistFriendship(login, user.getLogin());

        if (friendsUser == null){
            Friend_user newFriend = new Friend_user();
            newFriend.setFriendship(false);
            friendsUser = newFriend;
        }

        return new ResponseEntity<>(friendsUser,HttpStatus.OK);
    }
}
