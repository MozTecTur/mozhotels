package com.mozhotels.srit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InstanceInfo.
 */
@Entity
@Table(name = "instance_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceinfo")
public class InstanceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_info_name", nullable = false)
    private String instanceInfoName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private InstanceInfoType instanceInfoType;

    @ManyToOne
    private InstanceTur instanceTur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceInfoName() {
        return instanceInfoName;
    }

    public void setInstanceInfoName(String instanceInfoName) {
        this.instanceInfoName = instanceInfoName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InstanceInfoType getInstanceInfoType() {
        return instanceInfoType;
    }

    public void setInstanceInfoType(InstanceInfoType instanceInfoType) {
        this.instanceInfoType = instanceInfoType;
    }

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceInfo instanceInfo = (InstanceInfo) o;
        if(instanceInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceInfo{" +
            "id=" + id +
            ", instanceInfoName='" + instanceInfoName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
