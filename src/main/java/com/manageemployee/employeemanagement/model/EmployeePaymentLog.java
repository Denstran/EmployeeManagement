package com.manageemployee.employeemanagement.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EMPLOYEE_PAYMENT_LOG")
@Getter
@Setter
public class EmployeePaymentLog extends BasePaymentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_TYPE_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_EMPLOYEE_PAYMENT_LOG_PAYMENT_TYPE"))
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false, foreignKey =
    @ForeignKey(name = "FK_EMPLOYEE_PAYMENT_LOG_EMPLOYEE"))
    private Employee employee;
}
