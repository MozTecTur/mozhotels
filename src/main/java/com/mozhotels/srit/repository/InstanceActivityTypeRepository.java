package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceActivityType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceActivityType entity.
 */
public interface InstanceActivityTypeRepository extends JpaRepository<InstanceActivityType,Long> {

}
