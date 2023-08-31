package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "PAYMENT_LOG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLog {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_TYPE_ID", nullable = false)
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @PrePersist
    @PreUpdate
    public void setDefaultDateOfPayment() {
        dateOfPayment = new Date();
    }
}
