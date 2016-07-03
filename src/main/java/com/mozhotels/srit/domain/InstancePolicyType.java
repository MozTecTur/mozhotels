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

/**
 * A InstancePolicyType.
 */
@Entity
@Table(name = "instance_policy_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancepolicytype")
public class InstancePolicyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_policy_type_name", nullable = false)
    private String instancePolicyTypeName;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "instancePolicyType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstancePolicy> instancePolicys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstancePolicyTypeName() {
        return instancePolicyTypeName;
    }

    public void setInstancePolicyTypeName(String instancePolicyTypeName) {
        this.instancePolicyTypeName = instancePolicyTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InstancePolicy> getInstancePolicys() {
        return instancePolicys;
    }

    public void setInstancePolicys(Set<InstancePolicy> instancePolicys) {
        this.instancePolicys = instancePolicys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstancePolicyType instancePolicyType = (InstancePolicyType) o;
        if(instancePolicyType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instancePolicyType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstancePolicyType{" +
            "id=" + id +
            ", instancePolicyTypeName='" + instancePolicyTypeName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
