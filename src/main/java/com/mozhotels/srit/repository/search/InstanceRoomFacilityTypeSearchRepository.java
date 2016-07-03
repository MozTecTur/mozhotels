package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceRoomFacilityType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceRoomFacilityType entity.
 */
public interface InstanceRoomFacilityTypeSearchRepository extends ElasticsearchRepository<InstanceRoomFacilityType, Long> {
}
