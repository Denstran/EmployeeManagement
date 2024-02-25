package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
public class BasePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(
            converter = MoneyConverter.class
    )
    @Column(name = "PAYMENT_AMOUNT", length = 63)
    private Money paymentAmount;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "DATE_OF_PAYMENT", updatable = false, insertable = false)
    private Date dateOfPayment;

    @PrePersist
    @PreUpdate
    public void setDefaultDateOfPayment() {
        dateOfPayment = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasePaymentEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
