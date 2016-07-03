package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.Picture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Picture entity.
 */
public interface PictureSearchRepository extends ElasticsearchRepository<Picture, Long> {
}
