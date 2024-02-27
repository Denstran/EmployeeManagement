package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.enumTypes.TransferAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "DATE_OF_PAYMENT", updatable = false)
    private Date dateOfPayment;

    @NotNull
    @Column(name = "TRANSFER_ACTION")
    @Enumerated(value = EnumType.STRING)
    private TransferAction transferAction;

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
