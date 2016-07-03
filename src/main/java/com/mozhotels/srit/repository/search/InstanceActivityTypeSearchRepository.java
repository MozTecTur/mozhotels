package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceActivityType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceActivityType entity.
 */
public interface InstanceActivityTypeSearchRepository extends ElasticsearchRepository<InstanceActivityType, Long> {
}
