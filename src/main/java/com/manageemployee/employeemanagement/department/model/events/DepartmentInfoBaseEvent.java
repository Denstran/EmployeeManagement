package com.manageemployee.employeemanagement.department.model.events;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;

@Data
public abstract class DepartmentInfoBaseEvent {
    private final CompanyBranch companyBranch;
    private final Department department;
    private final Money oldBudget;
    private final Money newBudget;
}
