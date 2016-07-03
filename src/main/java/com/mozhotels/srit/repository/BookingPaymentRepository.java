package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.BookingPayment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BookingPayment entity.
 */
public interface BookingPaymentRepository extends JpaRepository<BookingPayment,Long> {

}
