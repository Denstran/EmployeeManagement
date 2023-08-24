package com.manageemployee.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DepartmentDTO {
    private Long id;
    private String departmentName;
    private Date lastModified;
    private Long companyBranchId;
    private Set<EmployeeDTO> employees;
}
