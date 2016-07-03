package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceRoomFacility;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceRoomFacility entity.
 */
public interface InstanceRoomFacilitySearchRepository extends ElasticsearchRepository<InstanceRoomFacility, Long> {
}
