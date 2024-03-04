package com.manageemployee.employeemanagement.model.events.departmentInfoEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public class DepartmentInfoRemoved {
    private final CompanyBranch companyBranch;
    private final Department department;
    private final Money departmentBudget;
}
