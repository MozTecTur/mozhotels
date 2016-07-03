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

import com.mozhotels.srit.domain.enumeration.ACountry;

import com.mozhotels.srit.domain.enumeration.HLanguage;

import com.mozhotels.srit.domain.enumeration.ECurrency;

/**
 * A Tourist.
 */
@Entity
@Table(name = "tourist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tourist")
public class Tourist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country_residence", nullable = false)
    private ACountry countryResidence;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country_booking", nullable = false)
    private ACountry countryBooking;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private HLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private ECurrency currency;

    @OneToMany(mappedBy = "tourist")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Booking> bookings = new HashSet<>();

    @OneToMany(mappedBy = "tourist")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceReview> instanceReviews = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ACountry getCountryResidence() {
        return countryResidence;
    }

    public void setCountryResidence(ACountry countryResidence) {
        this.countryResidence = countryResidence;
    }

    public ACountry getCountryBooking() {
        return countryBooking;
    }

    public void setCountryBooking(ACountry countryBooking) {
        this.countryBooking = countryBooking;
    }

    public HLanguage getLanguage() {
        return language;
    }

    public void setLanguage(HLanguage language) {
        this.language = language;
    }

    public ECurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ECurrency currency) {
        this.currency = currency;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<InstanceReview> getInstanceReviews() {
        return instanceReviews;
    }

    public void setInstanceReviews(Set<InstanceReview> instanceReviews) {
        this.instanceReviews = instanceReviews;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tourist tourist = (Tourist) o;
        if(tourist.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tourist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tourist{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", countryResidence='" + countryResidence + "'" +
            ", countryBooking='" + countryBooking + "'" +
            ", language='" + language + "'" +
            ", currency='" + currency + "'" +
            '}';
    }
}
