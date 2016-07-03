package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.GuestTourist;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GuestTourist entity.
 */
public interface GuestTouristRepository extends JpaRepository<GuestTourist,Long> {

}
