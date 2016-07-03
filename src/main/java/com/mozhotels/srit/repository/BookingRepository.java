package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.Booking;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Booking entity.
 */
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Query("select distinct booking from Booking booking left join fetch booking.instanceRoomTypes left join fetch booking.instanceRoomFacilitys")
    List<Booking> findAllWithEagerRelationships();

    @Query("select booking from Booking booking left join fetch booking.instanceRoomTypes left join fetch booking.instanceRoomFacilitys where booking.id =:id")
    Booking findOneWithEagerRelationships(@Param("id") Long id);

}
