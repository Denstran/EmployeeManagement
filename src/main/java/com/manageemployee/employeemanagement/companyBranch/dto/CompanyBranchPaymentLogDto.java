package com.manageemployee.employeemanagement.companyBranch.dto;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
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

/**
 * DTO for {@link CompanyBranchPaymentLog}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyBranchPaymentLogDto {
    private Long id;
    private Money paymentAmount;
    @FutureOrPresent(message = "Дата платежа не может быть в прошлом!")
    private Date dateOfPayment;
    @NotNull(message = "Тип платежа не можте быть пустым!")
    private PaymentType paymentType;
    private Long companyBranchId;
    @NotNull
    private TransferAction transferAction;
}