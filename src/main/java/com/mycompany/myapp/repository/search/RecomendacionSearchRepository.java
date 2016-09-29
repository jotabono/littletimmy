package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Recomendacion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Recomendacion entity.
 */
public interface RecomendacionSearchRepository extends ElasticsearchRepository<Recomendacion, Long> {
}
