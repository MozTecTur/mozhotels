package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.Tourist;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tourist entity.
 */
public interface TouristRepository extends JpaRepository<Tourist,Long> {

}
