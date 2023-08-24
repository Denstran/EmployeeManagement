package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeStatusDTO {
    private Long id;
    private EEmployeeStatus employeeStatus;
}
