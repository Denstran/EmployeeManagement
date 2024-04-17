package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.util.enumType.PaymentType;
import com.manageemployee.employeemanagement.util.enumType.TransferAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
public abstract class BasePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Тип платежа не может быть пустым!")
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "PAYMENT_AMOUNT"))
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
