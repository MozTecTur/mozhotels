package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.LocalTur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LocalTur entity.
 */
public interface LocalTurRepository extends JpaRepository<LocalTur,Long> {

}
