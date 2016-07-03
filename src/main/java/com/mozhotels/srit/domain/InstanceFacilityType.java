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
 * A InstanceFacilityType.
 */
@Entity
@Table(name = "instance_facility_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancefacilitytype")
public class InstanceFacilityType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_facility_type_name", nullable = false)
    private String instanceFacilityTypeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "facility", nullable = false)
    private BFacility facility;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "instanceFacilityType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceFacility> instanceFacilitys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceFacilityTypeName() {
        return instanceFacilityTypeName;
    }

    public void setInstanceFacilityTypeName(String instanceFacilityTypeName) {
        this.instanceFacilityTypeName = instanceFacilityTypeName;
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

    public Set<InstanceFacility> getInstanceFacilitys() {
        return instanceFacilitys;
    }

    public void setInstanceFacilitys(Set<InstanceFacility> instanceFacilitys) {
        this.instanceFacilitys = instanceFacilitys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceFacilityType instanceFacilityType = (InstanceFacilityType) o;
        if(instanceFacilityType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceFacilityType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceFacilityType{" +
            "id=" + id +
            ", instanceFacilityTypeName='" + instanceFacilityTypeName + "'" +
            ", facility='" + facility + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
