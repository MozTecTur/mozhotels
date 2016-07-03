package com.mozhotels.srit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.mozhotels.srit.domain.enumeration.JPaymentType;

import com.mozhotels.srit.domain.enumeration.ECurrency;

import com.mozhotels.srit.domain.enumeration.KPaymentState;

import com.mozhotels.srit.domain.enumeration.LCardType;

/**
 * A BookingPayment.
 */
@Entity
@Table(name = "booking_payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bookingpayment")
public class BookingPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private JPaymentType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private ECurrency currency;

    @NotNull
    @Column(name = "amount", precision=10, scale=2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private KPaymentState state;

    @NotNull
    @Column(name = "card_holder", nullable = false)
    private String cardHolder;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private LCardType cardType;

    @NotNull
    @Column(name = "card_number", nullable = false)
    private Integer cardNumber;

    @NotNull
    @Column(name = "card_expiry", nullable = false)
    private Integer cardExpiry;

    @NotNull
    @Column(name = "card_ccv", nullable = false)
    private Integer cardCCV;

    @OneToOne
    @JoinColumn(unique = true)
    private Booking booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JPaymentType getType() {
        return type;
    }

    public void setType(JPaymentType type) {
        this.type = type;
    }

    public ECurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ECurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public KPaymentState getState() {
        return state;
    }

    public void setState(KPaymentState state) {
        this.state = state;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public LCardType getCardType() {
        return cardType;
    }

    public void setCardType(LCardType cardType) {
        this.cardType = cardType;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(Integer cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public Integer getCardCCV() {
        return cardCCV;
    }

    public void setCardCCV(Integer cardCCV) {
        this.cardCCV = cardCCV;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookingPayment bookingPayment = (BookingPayment) o;
        if(bookingPayment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bookingPayment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookingPayment{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", currency='" + currency + "'" +
            ", amount='" + amount + "'" +
            ", date='" + date + "'" +
            ", state='" + state + "'" +
            ", cardHolder='" + cardHolder + "'" +
            ", cardType='" + cardType + "'" +
            ", cardNumber='" + cardNumber + "'" +
            ", cardExpiry='" + cardExpiry + "'" +
            ", cardCCV='" + cardCCV + "'" +
            '}';
    }
}
