package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.Province;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Province entity.
 */
public interface ProvinceRepository extends JpaRepository<Province,Long> {

}
