package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Chat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Chat entity.
 */
public interface ChatSearchRepository extends ElasticsearchRepository<Chat, Long> {
}
