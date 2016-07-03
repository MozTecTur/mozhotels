package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceTur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceTur entity.
 */
public interface InstanceTurRepository extends JpaRepository<InstanceTur,Long> {

}
