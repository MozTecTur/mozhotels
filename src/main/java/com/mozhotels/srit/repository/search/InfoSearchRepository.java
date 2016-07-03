package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.Info;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Info entity.
 */
public interface InfoSearchRepository extends ElasticsearchRepository<Info, Long> {
}
