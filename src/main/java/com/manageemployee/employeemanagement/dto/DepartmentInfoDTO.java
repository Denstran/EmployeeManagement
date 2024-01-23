package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentInfoDTO {

    @NotNull(message = "Филиал не должен отсутствовать!")
    private Long companyBranchId;

    @NotNull(message = "Отдел не должен отсутствовать!")
    private Long departmentId;

    @NotNull
    @Valid
    private Money departmentBudget;
    private int amountOfEmployee;
}
