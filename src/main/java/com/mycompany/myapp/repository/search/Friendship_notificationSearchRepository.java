package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Friendship_notification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Friendship_notification entity.
 */
public interface Friendship_notificationSearchRepository extends ElasticsearchRepository<Friendship_notification, Long> {
}
