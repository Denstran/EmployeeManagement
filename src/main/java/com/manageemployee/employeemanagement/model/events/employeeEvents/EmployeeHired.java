package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;

public class EmployeeHired extends EmployeeBaseEvent {

    public EmployeeHired(Money salary, Department department, CompanyBranch companyBranch) {
        super(salary, department, companyBranch);
    }
}
