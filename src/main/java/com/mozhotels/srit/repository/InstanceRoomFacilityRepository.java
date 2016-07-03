package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceRoomFacility;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceRoomFacility entity.
 */
public interface InstanceRoomFacilityRepository extends JpaRepository<InstanceRoomFacility,Long> {

}
