package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Centro;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Centro entity.
 */
public interface CentroSearchRepository extends ElasticsearchRepository<Centro, Long> {
}
