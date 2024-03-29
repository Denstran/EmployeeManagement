package com.manageemployee.employeemanagement.employee.dto;

import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.enumType.PaymentType;
import com.manageemployee.employeemanagement.util.enumType.TransferAction;
import jakarta.validation.constraints.FutureOrPresent;
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
    private Money paymentAmount;
    @FutureOrPresent(message = "Перевод средств не может быть в прошлом!")
    private Date dateOfPayment;

    @NotNull(message = "Тип платежа не может быть пустым!")
    private PaymentType paymentType;

    private String employeeName;
    private String employeePhoneNumber;

    @NotNull(message = "Сотрудник не может отсутствовать!")
    private Long employeeId;
    @NotNull
    private TransferAction transferAction;
}