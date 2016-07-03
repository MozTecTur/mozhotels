package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.Booking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Booking entity.
 */
public interface BookingSearchRepository extends ElasticsearchRepository<Booking, Long> {
}
