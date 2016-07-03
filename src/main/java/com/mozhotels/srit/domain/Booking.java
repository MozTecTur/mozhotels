package com.mozhotels.srit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mozhotels.srit.domain.enumeration.IBookingState;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "booking")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "check_in", nullable = false)
    private ZonedDateTime checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private ZonedDateTime checkOut;

    @NotNull
    @Column(name = "people_adult", nullable = false)
    private Integer peopleAdult;

    @NotNull
    @Column(name = "people_child", nullable = false)
    private Integer peopleChild;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "edit_date")
    private ZonedDateTime editDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private IBookingState state;

    @NotNull
    @Column(name = "rooms", nullable = false)
    private Integer rooms;

    @NotNull
    @Column(name = "tax", nullable = false)
    private Float tax;

    @NotNull
    @Column(name = "total_price", precision=10, scale=2, nullable = false)
    private BigDecimal totalPrice;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "booking_instance_room_type",
               joinColumns = @JoinColumn(name="bookings_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="instance_room_types_id", referencedColumnName="ID"))
    private Set<InstanceRoomType> instanceRoomTypes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "booking_instance_room_facility",
               joinColumns = @JoinColumn(name="bookings_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="instance_room_facilitys_id", referencedColumnName="ID"))
    private Set<InstanceRoomFacility> instanceRoomFacilitys = new HashSet<>();

    @ManyToOne
    private Tourist tourist;

    @ManyToOne
    private GuestTourist guestTourist;

    @ManyToOne
    private InstanceTur instanceTur;

    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    private BookingPayment bookingPayment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(ZonedDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public ZonedDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(ZonedDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getPeopleAdult() {
        return peopleAdult;
    }

    public void setPeopleAdult(Integer peopleAdult) {
        this.peopleAdult = peopleAdult;
    }

    public Integer getPeopleChild() {
        return peopleChild;
    }

    public void setPeopleChild(Integer peopleChild) {
        this.peopleChild = peopleChild;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(ZonedDateTime editDate) {
        this.editDate = editDate;
    }

    public IBookingState getState() {
        return state;
    }

    public void setState(IBookingState state) {
        this.state = state;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<InstanceRoomType> getInstanceRoomTypes() {
        return instanceRoomTypes;
    }

    public void setInstanceRoomTypes(Set<InstanceRoomType> instanceRoomTypes) {
        this.instanceRoomTypes = instanceRoomTypes;
    }

    public Set<InstanceRoomFacility> getInstanceRoomFacilitys() {
        return instanceRoomFacilitys;
    }

    public void setInstanceRoomFacilitys(Set<InstanceRoomFacility> instanceRoomFacilitys) {
        this.instanceRoomFacilitys = instanceRoomFacilitys;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public GuestTourist getGuestTourist() {
        return guestTourist;
    }

    public void setGuestTourist(GuestTourist guestTourist) {
        this.guestTourist = guestTourist;
    }

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    public BookingPayment getBookingPayment() {
        return bookingPayment;
    }

    public void setBookingPayment(BookingPayment bookingPayment) {
        this.bookingPayment = bookingPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        if(booking.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + id +
            ", checkIn='" + checkIn + "'" +
            ", checkOut='" + checkOut + "'" +
            ", peopleAdult='" + peopleAdult + "'" +
            ", peopleChild='" + peopleChild + "'" +
            ", createdDate='" + createdDate + "'" +
            ", editDate='" + editDate + "'" +
            ", state='" + state + "'" +
            ", rooms='" + rooms + "'" +
            ", tax='" + tax + "'" +
            ", totalPrice='" + totalPrice + "'" +
            '}';
    }
}
