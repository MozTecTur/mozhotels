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

import com.mozhotels.srit.domain.enumeration.CRating;

import com.mozhotels.srit.domain.enumeration.ECurrency;

/**
 * A InstanceTur.
 */
@Entity
@Table(name = "instance_tur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancetur")
public class InstanceTur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_tur_name", nullable = false)
    private String instanceTurName;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "email")
    private String email;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "rooms")
    private Integer rooms;

    @Column(name = "beds")
    private Integer beds;

    @Column(name = "floors")
    private Integer floors;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private CRating rating;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private ECurrency currency;

    @Column(name = "contact_number_principal")
    private Integer contactNumberPrincipal;

    @Lob
    @Column(name = "photo_principal")
    private byte[] photoPrincipal;

    @Column(name = "photo_principal_content_type")    
    private String photoPrincipalContentType;

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Picture> pictures = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceContact> instanceContacts = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceFacility> instanceResources = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceActivity> instanceActivitys = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceRoomType> instanceRoomTypes = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstancePolicy> instancePolicys = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceInfo> instanceInfos = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceReview> instanceReviews = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Booking> bookings = new HashSet<>();

    @ManyToOne
    private LocalTur localTur;

    @ManyToOne
    private InstanceTurType instanceTurType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceTurName() {
        return instanceTurName;
    }

    public void setInstanceTurName(String instanceTurName) {
        this.instanceTurName = instanceTurName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public CRating getRating() {
        return rating;
    }

    public void setRating(CRating rating) {
        this.rating = rating;
    }

    public ECurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ECurrency currency) {
        this.currency = currency;
    }

    public Integer getContactNumberPrincipal() {
        return contactNumberPrincipal;
    }

    public void setContactNumberPrincipal(Integer contactNumberPrincipal) {
        this.contactNumberPrincipal = contactNumberPrincipal;
    }

    public byte[] getPhotoPrincipal() {
        return photoPrincipal;
    }

    public void setPhotoPrincipal(byte[] photoPrincipal) {
        this.photoPrincipal = photoPrincipal;
    }

    public String getPhotoPrincipalContentType() {
        return photoPrincipalContentType;
    }

    public void setPhotoPrincipalContentType(String photoPrincipalContentType) {
        this.photoPrincipalContentType = photoPrincipalContentType;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public Set<InstanceContact> getInstanceContacts() {
        return instanceContacts;
    }

    public void setInstanceContacts(Set<InstanceContact> instanceContacts) {
        this.instanceContacts = instanceContacts;
    }

    public Set<InstanceFacility> getInstanceResources() {
        return instanceResources;
    }

    public void setInstanceResources(Set<InstanceFacility> instanceFacilitys) {
        this.instanceResources = instanceFacilitys;
    }

    public Set<InstanceActivity> getInstanceActivitys() {
        return instanceActivitys;
    }

    public void setInstanceActivitys(Set<InstanceActivity> instanceActivitys) {
        this.instanceActivitys = instanceActivitys;
    }

    public Set<InstanceRoomType> getInstanceRoomTypes() {
        return instanceRoomTypes;
    }

    public void setInstanceRoomTypes(Set<InstanceRoomType> instanceRoomTypes) {
        this.instanceRoomTypes = instanceRoomTypes;
    }

    public Set<InstancePolicy> getInstancePolicys() {
        return instancePolicys;
    }

    public void setInstancePolicys(Set<InstancePolicy> instancePolicys) {
        this.instancePolicys = instancePolicys;
    }

    public Set<InstanceInfo> getInstanceInfos() {
        return instanceInfos;
    }

    public void setInstanceInfos(Set<InstanceInfo> instanceInfos) {
        this.instanceInfos = instanceInfos;
    }

    public Set<InstanceReview> getInstanceReviews() {
        return instanceReviews;
    }

    public void setInstanceReviews(Set<InstanceReview> instanceReviews) {
        this.instanceReviews = instanceReviews;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public LocalTur getLocalTur() {
        return localTur;
    }

    public void setLocalTur(LocalTur localTur) {
        this.localTur = localTur;
    }

    public InstanceTurType getInstanceTurType() {
        return instanceTurType;
    }

    public void setInstanceTurType(InstanceTurType instanceTurType) {
        this.instanceTurType = instanceTurType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceTur instanceTur = (InstanceTur) o;
        if(instanceTur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceTur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceTur{" +
            "id=" + id +
            ", instanceTurName='" + instanceTurName + "'" +
            ", description='" + description + "'" +
            ", address='" + address + "'" +
            ", website='" + website + "'" +
            ", email='" + email + "'" +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            ", rooms='" + rooms + "'" +
            ", beds='" + beds + "'" +
            ", floors='" + floors + "'" +
            ", rating='" + rating + "'" +
            ", currency='" + currency + "'" +
            ", contactNumberPrincipal='" + contactNumberPrincipal + "'" +
            ", photoPrincipal='" + photoPrincipal + "'" +
            ", photoPrincipalContentType='" + photoPrincipalContentType + "'" +
            '}';
    }
}
