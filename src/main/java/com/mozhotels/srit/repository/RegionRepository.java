package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.Region;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Region entity.
 */
public interface RegionRepository extends JpaRepository<Region,Long> {

}
