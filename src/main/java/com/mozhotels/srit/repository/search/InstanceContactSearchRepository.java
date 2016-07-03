package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.InstanceContact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceContact entity.
 */
public interface InstanceContactSearchRepository extends ElasticsearchRepository<InstanceContact, Long> {
}
