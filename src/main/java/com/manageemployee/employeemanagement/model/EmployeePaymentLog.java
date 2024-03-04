package com.manageemployee.employeemanagement.model;


import com.manageemployee.employeemanagement.model.enumTypes.PaymentType;
import com.manageemployee.employeemanagement.model.enumTypes.TransferAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EMPLOYEE_PAYMENT_LOG")
@Getter
@Setter
public class EmployeePaymentLog extends BasePaymentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false, foreignKey =
    @ForeignKey(name = "FK_EMPLOYEE_PAYMENT_LOG_EMPLOYEE"))
    private Employee employee;

    public static EmployeePaymentLog createPaymentLog(Employee employee, Money amount, boolean isPositive) {
        EmployeePaymentLog paymentLog = new EmployeePaymentLog();
        paymentLog.setEmployee(employee);
        paymentLog.setPaymentType(PaymentType.SALARY_CHANGES);
        paymentLog.setPaymentAmount(amount);
        paymentLog.setTransferAction(isPositive ? TransferAction.INCREASE : TransferAction.DECREASE);

        return paymentLog;
    }
}
