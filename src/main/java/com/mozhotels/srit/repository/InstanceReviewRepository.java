package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceReview;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceReview entity.
 */
public interface InstanceReviewRepository extends JpaRepository<InstanceReview,Long> {

}
