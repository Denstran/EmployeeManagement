package com.manageemployee.employeemanagement.model.events.departmentInfoEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public abstract class DepartmentInfoBaseEvent {
    private final CompanyBranch companyBranch;
    private final Department department;
    private final Money oldBudget;
    private final Money newBudget;
}
