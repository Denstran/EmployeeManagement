package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.enumTypes.EPaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTypeDTO {
    private Long id;
    private EPaymentType paymentType;
}
