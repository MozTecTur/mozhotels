package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceFacility;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceFacility entity.
 */
public interface InstanceFacilitySearchRepository extends ElasticsearchRepository<InstanceFacility, Long> {
}
