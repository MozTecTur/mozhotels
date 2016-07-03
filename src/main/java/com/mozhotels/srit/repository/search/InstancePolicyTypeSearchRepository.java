package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstancePolicyType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstancePolicyType entity.
 */
public interface InstancePolicyTypeSearchRepository extends ElasticsearchRepository<InstancePolicyType, Long> {
}
