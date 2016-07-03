package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceInfo entity.
 */
public interface InstanceInfoRepository extends JpaRepository<InstanceInfo,Long> {

}
