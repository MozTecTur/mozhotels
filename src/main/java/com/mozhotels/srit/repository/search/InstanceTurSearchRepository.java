package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceTur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceTur entity.
 */
public interface InstanceTurSearchRepository extends ElasticsearchRepository<InstanceTur, Long> {
}
