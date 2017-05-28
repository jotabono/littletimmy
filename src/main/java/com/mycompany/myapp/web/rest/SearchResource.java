package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jotabono on 25/5/17.
 */


@RestController
@RequestMapping("/api")
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    @Inject
    private UserRepository userRepository;

    public List<User> searchUsers(String query) {
        //log.debug("REST request to search Playlists for query {}", query);
        return userRepository.findAll()
            .stream()
            .filter(user -> user.getFirstName().toLowerCase().contains(query))
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/_search/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Object> search(@PathVariable String query) {
        //log.debug("REST request to search Playlists for query {}", query);

        query = query.toLowerCase();

        List<User> users = searchUsers(query);

        Set<Object> res = new HashSet<>();

        res.addAll(searchUsers(query));

        return res;
    }

    @RequestMapping(value = "/_search/categories/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public HashMap<String, Object> searchCategories(@PathVariable String query) {
        log.debug("REST request to search Playlists for query {}", query);

        query = query.toLowerCase();

        List<User> users = searchUsers(query);

        Set<Object> res = new HashSet<>();

        users.removeIf(user -> user.getFirstName().equals("anonymousUser"));

        HashMap map = new HashMap();

        map.put("users", searchUsers(query));

        return map;
    }
}
