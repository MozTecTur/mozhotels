package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.Picture;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Picture entity.
 */
public interface PictureRepository extends JpaRepository<Picture,Long> {

}
