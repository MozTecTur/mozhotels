package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstancePolicy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstancePolicy entity.
 */
public interface InstancePolicyRepository extends JpaRepository<InstancePolicy,Long> {

}
