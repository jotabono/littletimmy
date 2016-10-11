package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Friend_user;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Friend_user entity.
 */
public interface Friend_userSearchRepository extends ElasticsearchRepository<Friend_user, Long> {
}
