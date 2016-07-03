package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceRoomType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceRoomType entity.
 */
public interface InstanceRoomTypeSearchRepository extends ElasticsearchRepository<InstanceRoomType, Long> {
}
