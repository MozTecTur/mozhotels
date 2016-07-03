package com.mozhotels.srit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mozhotels.srit.domain.enumeration.BFacility;

/**
 * A InstanceRoomFacilityType.
 */
@Entity
@Table(name = "instance_room_facility_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceroomfacilitytype")
public class InstanceRoomFacilityType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_room_facility_type_name", nullable = false)
    private String instanceRoomFacilityTypeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "facility", nullable = false)
    private BFacility facility;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "instanceRoomFacilityType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceRoomFacility> instanceRoomFacilitys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceRoomFacilityTypeName() {
        return instanceRoomFacilityTypeName;
    }

    public void setInstanceRoomFacilityTypeName(String instanceRoomFacilityTypeName) {
        this.instanceRoomFacilityTypeName = instanceRoomFacilityTypeName;
    }

    public BFacility getFacility() {
        return facility;
    }

    public void setFacility(BFacility facility) {
        this.facility = facility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InstanceRoomFacility> getInstanceRoomFacilitys() {
        return instanceRoomFacilitys;
    }

    public void setInstanceRoomFacilitys(Set<InstanceRoomFacility> instanceRoomFacilitys) {
        this.instanceRoomFacilitys = instanceRoomFacilitys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceRoomFacilityType instanceRoomFacilityType = (InstanceRoomFacilityType) o;
        if(instanceRoomFacilityType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceRoomFacilityType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceRoomFacilityType{" +
            "id=" + id +
            ", instanceRoomFacilityTypeName='" + instanceRoomFacilityTypeName + "'" +
            ", facility='" + facility + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
