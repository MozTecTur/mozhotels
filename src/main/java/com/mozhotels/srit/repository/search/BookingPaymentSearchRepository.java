package com.mozhotels.srit.repository.search;

import com.mozhotels.srit.domain.BookingPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BookingPayment entity.
 */
public interface BookingPaymentSearchRepository extends ElasticsearchRepository<BookingPayment, Long> {
}
