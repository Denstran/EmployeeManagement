package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.enumTypes.TransferAction;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DTO for {@link com.manageemployee.employeemanagement.model.CompanyBranchPaymentLog}
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
    private PaymentTypeDTO paymentType;
    private Long companyBranchId;
    @NotNull
    private TransferAction transferAction;
}