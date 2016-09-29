package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Estudios;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Estudios entity.
 */
public interface EstudiosSearchRepository extends ElasticsearchRepository<Estudios, Long> {
}
