package com.mozhotels.srit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mozhotels.srit.domain.enumeration.BFacility;

import com.mozhotels.srit.domain.enumeration.FInstanceArea;

/**
 * A InstanceRoomFacility.
 */
@Entity
@Table(name = "instance_room_facility")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceroomfacility")
public class InstanceRoomFacility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_room_facility_name", nullable = false)
    private String instanceRoomFacilityName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "facility", nullable = false)
    private BFacility facility;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "area")
    private FInstanceArea area;

    @NotNull
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @ManyToOne
    private InstanceRoomFacilityType instanceRoomFacilityType;

    @ManyToMany(mappedBy = "instanceRoomFacilitys")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceRoomType> instanceRoomTypes = new HashSet<>();

    @ManyToMany(mappedBy = "instanceRoomFacilitys")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Booking> bookings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceRoomFacilityName() {
        return instanceRoomFacilityName;
    }

    public void setInstanceRoomFacilityName(String instanceRoomFacilityName) {
        this.instanceRoomFacilityName = instanceRoomFacilityName;
    }

    public BFacility getFacility() {
        return facility;
    }

    public void setFacility(BFacility facility) {
        this.facility = facility;
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

    public InstanceRoomFacilityType getInstanceRoomFacilityType() {
        return instanceRoomFacilityType;
    }

    public void setInstanceRoomFacilityType(InstanceRoomFacilityType instanceRoomFacilityType) {
        this.instanceRoomFacilityType = instanceRoomFacilityType;
    }

    public Set<InstanceRoomType> getInstanceRoomTypes() {
        return instanceRoomTypes;
    }

    public void setInstanceRoomTypes(Set<InstanceRoomType> instanceRoomTypes) {
        this.instanceRoomTypes = instanceRoomTypes;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceRoomFacility instanceRoomFacility = (InstanceRoomFacility) o;
        if(instanceRoomFacility.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceRoomFacility.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceRoomFacility{" +
            "id=" + id +
            ", instanceRoomFacilityName='" + instanceRoomFacilityName + "'" +
            ", facility='" + facility + "'" +
            ", quantity='" + quantity + "'" +
            ", area='" + area + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
