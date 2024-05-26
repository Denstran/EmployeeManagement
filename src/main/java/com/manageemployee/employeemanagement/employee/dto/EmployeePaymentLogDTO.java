package com.manageemployee.employeemanagement.employee.dto;

import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.enumType.PaymentType;
import com.manageemployee.employeemanagement.util.enumType.TransferAction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeePaymentLogDTO {
    private Long id;

    @NotNull(message = "Переводимые средства не должны быть пустыми!")
    @Valid
    private Money paymentAmount;

    @NotNull(message = "Тип платежа не может быть пустым!")
    private PaymentType paymentType;

    @NotNull(message = "Сотрудник не может отсутствовать!")
    private Long employeeId;

    private TransferAction transferAction;
    private String employeeName;
    private String employeePhoneNumber;
    private Date dateOfPayment;
}