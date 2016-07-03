package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceRoomFacilityType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceRoomFacilityType entity.
 */
public interface InstanceRoomFacilityTypeRepository extends JpaRepository<InstanceRoomFacilityType,Long> {

}
