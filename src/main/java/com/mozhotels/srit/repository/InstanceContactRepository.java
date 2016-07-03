package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceContact;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceContact entity.
 */
public interface InstanceContactRepository extends JpaRepository<InstanceContact,Long> {

}
