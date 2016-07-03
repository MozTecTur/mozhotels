package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstancePolicyType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstancePolicyType entity.
 */
public interface InstancePolicyTypeRepository extends JpaRepository<InstancePolicyType,Long> {

}
