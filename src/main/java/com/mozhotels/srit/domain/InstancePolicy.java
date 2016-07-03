package com.mozhotels.srit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InstancePolicy.
 */
@Entity
@Table(name = "instance_policy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancepolicy")
public class InstancePolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_policty_name", nullable = false)
    private String instancePolictyName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private InstancePolicyType instancePolicyType;

    @ManyToOne
    private InstanceTur instanceTur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstancePolictyName() {
        return instancePolictyName;
    }

    public void setInstancePolictyName(String instancePolictyName) {
        this.instancePolictyName = instancePolictyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InstancePolicyType getInstancePolicyType() {
        return instancePolicyType;
    }

    public void setInstancePolicyType(InstancePolicyType instancePolicyType) {
        this.instancePolicyType = instancePolicyType;
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
        InstancePolicy instancePolicy = (InstancePolicy) o;
        if(instancePolicy.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instancePolicy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstancePolicy{" +
            "id=" + id +
            ", instancePolictyName='" + instancePolictyName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
