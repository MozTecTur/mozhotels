package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstancePolicy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstancePolicy entity.
 */
public interface InstancePolicySearchRepository extends ElasticsearchRepository<InstancePolicy, Long> {
}
