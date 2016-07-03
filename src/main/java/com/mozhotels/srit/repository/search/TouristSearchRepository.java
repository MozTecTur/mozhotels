package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.Tourist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Tourist entity.
 */
public interface TouristSearchRepository extends ElasticsearchRepository<Tourist, Long> {
}
