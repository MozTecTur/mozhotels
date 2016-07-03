package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceInfoType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceInfoType entity.
 */
public interface InstanceInfoTypeRepository extends JpaRepository<InstanceInfoType,Long> {

}
