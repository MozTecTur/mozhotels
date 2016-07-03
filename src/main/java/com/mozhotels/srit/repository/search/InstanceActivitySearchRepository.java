package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceActivity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceActivity entity.
 */
public interface InstanceActivitySearchRepository extends ElasticsearchRepository<InstanceActivity, Long> {
}
