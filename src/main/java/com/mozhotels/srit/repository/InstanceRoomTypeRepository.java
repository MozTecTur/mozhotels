package com.mozhotels.srit.repository;

import com.mozhotels.srit.domain.InstanceRoomType;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceRoomType entity.
 */
public interface InstanceRoomTypeRepository extends JpaRepository<InstanceRoomType,Long> {

    @Query("select distinct instanceRoomType from InstanceRoomType instanceRoomType left join fetch instanceRoomType.instanceRoomFacilitys")
    List<InstanceRoomType> findAllWithEagerRelationships();

    @Query("select instanceRoomType from InstanceRoomType instanceRoomType left join fetch instanceRoomType.instanceRoomFacilitys where instanceRoomType.id =:id")
    InstanceRoomType findOneWithEagerRelationships(@Param("id") Long id);

}
