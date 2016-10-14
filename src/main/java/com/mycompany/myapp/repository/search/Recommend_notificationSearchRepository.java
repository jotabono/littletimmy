package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Recommend_notification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Recommend_notification entity.
 */
public interface Recommend_notificationSearchRepository extends ElasticsearchRepository<Recommend_notification, Long> {
}
