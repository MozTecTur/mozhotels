package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceTurType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceTurType entity.
 */
public interface InstanceTurTypeSearchRepository extends ElasticsearchRepository<InstanceTurType, Long> {
}
