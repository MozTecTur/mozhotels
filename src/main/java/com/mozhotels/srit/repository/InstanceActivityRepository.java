package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceActivity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceActivity entity.
 */
public interface InstanceActivityRepository extends JpaRepository<InstanceActivity,Long> {

}
