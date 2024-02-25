package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public abstract class EmployeeBaseEvent {
    private final Money salary;
    private final Department department;
    private final CompanyBranch companyBranch;
}
