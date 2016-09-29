package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Trabajo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Trabajo entity.
 */
public interface TrabajoSearchRepository extends ElasticsearchRepository<Trabajo, Long> {
}
