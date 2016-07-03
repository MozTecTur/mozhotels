package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.Info;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Info entity.
 */
public interface InfoRepository extends JpaRepository<Info,Long> {

}
