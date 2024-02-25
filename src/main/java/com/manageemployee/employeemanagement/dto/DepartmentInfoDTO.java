package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.validationgroups.DefaultGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentInfoDTO {

    private String departmentName;

    @NotNull(message = "Филиал не должен отсутствовать!", groups = DefaultGroup.class)
    private Long companyBranchId;

    @NotNull(message = "Отдел не должен отсутствовать!", groups = DefaultGroup.class)
    private Long departmentId;

    @NotNull
    @Valid
    private Money departmentBudget;
    private int amountOfEmployee;
}
