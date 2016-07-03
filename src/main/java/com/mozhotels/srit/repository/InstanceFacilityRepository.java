package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceFacility;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceFacility entity.
 */
public interface InstanceFacilityRepository extends JpaRepository<InstanceFacility,Long> {

}
