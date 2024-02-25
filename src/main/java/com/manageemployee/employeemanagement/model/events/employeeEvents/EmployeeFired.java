package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;

public class EmployeeFired extends EmployeeBaseEvent{
    public EmployeeFired(Money salary, Department department, CompanyBranch companyBranch) {
        super(salary, department, companyBranch);
    }
}
