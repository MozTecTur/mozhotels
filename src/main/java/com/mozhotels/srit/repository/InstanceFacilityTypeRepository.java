package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceFacilityType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceFacilityType entity.
 */
public interface InstanceFacilityTypeRepository extends JpaRepository<InstanceFacilityType,Long> {

}
