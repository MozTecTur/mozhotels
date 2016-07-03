package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.GuestTourist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GuestTourist entity.
 */
public interface GuestTouristSearchRepository extends ElasticsearchRepository<GuestTourist, Long> {
}
