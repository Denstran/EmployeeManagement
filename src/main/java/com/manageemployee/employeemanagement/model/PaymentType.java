package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.model.enumTypes.EPaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "PAYMENT_TYPE")
    private EPaymentType paymentType;

    @OneToMany(mappedBy = "paymentType", cascade = CascadeType.ALL)
    Set<PaymentLog> payments = new HashSet<>();

    public void addPayment(PaymentLog paymentLog) {
        this.payments.add(paymentLog);
        paymentLog.setPaymentType(this);
    }

}
