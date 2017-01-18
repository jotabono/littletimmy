package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Messages;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Messages entity.
 */
public interface MessagesSearchRepository extends ElasticsearchRepository<Messages, Long> {
}
