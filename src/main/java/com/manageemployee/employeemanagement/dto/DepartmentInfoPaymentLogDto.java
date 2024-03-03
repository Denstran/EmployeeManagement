package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.enumTypes.PaymentType;
import com.manageemployee.employeemanagement.model.enumTypes.TransferAction;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DTO for {@link com.manageemployee.employeemanagement.model.DepartmentInfoPaymentLog}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartmentInfoPaymentLogDto {
    private Long id;

    @NotNull(message = "Переводимые средства не должны быть пустыми!")
    private Money paymentAmount;
    @FutureOrPresent(message = "Перевод средств не может быть в прошлом!")
    private Date dateOfPayment;
    @NotNull(message = "Тип платежа не может быть пустым!")
    private PaymentType paymentType;

    @NotNull(message = "Филиал не может отсутствовать!")
    private Long companyBranchId;
    @NotNull(message = "Отдел не может отсутствовать!")
    private Long departmentId;
    @NotNull
    private TransferAction transferAction;
}