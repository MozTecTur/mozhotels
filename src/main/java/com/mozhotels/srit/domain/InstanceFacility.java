package com.mozhotels.srit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.mozhotels.srit.domain.enumeration.FInstanceArea;

/**
 * A InstanceFacility.
 */
@Entity
@Table(name = "instance_facility")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancefacility")
public class InstanceFacility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_facility_name", nullable = false)
    private String instanceFacilityName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "area")
    private FInstanceArea area;

    @NotNull
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @ManyToOne
    private InstanceFacilityType instanceFacilityType;

    @ManyToOne
    private InstanceTur instanceTur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceFacilityName() {
        return instanceFacilityName;
    }

    public void setInstanceFacilityName(String instanceFacilityName) {
        this.instanceFacilityName = instanceFacilityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public FInstanceArea getArea() {
        return area;
    }

    public void setArea(FInstanceArea area) {
        this.area = area;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public InstanceFacilityType getInstanceFacilityType() {
        return instanceFacilityType;
    }

    public void setInstanceFacilityType(InstanceFacilityType instanceFacilityType) {
        this.instanceFacilityType = instanceFacilityType;
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
        InstanceFacility instanceFacility = (InstanceFacility) o;
        if(instanceFacility.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceFacility.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceFacility{" +
            "id=" + id +
            ", instanceFacilityName='" + instanceFacilityName + "'" +
            ", description='" + description + "'" +
            ", quantity='" + quantity + "'" +
            ", area='" + area + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
