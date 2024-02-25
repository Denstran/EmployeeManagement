package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.EmployeeStatus;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Getter;


@Getter
public class EmployeeDeleted extends EmployeeBaseEvent {

    private final EmployeeStatus employeeStatus;
    public EmployeeDeleted(Money salary, Department department, CompanyBranch companyBranch,
                           EmployeeStatus employeeStatus) {
        super(salary, department, companyBranch);
        this.employeeStatus = employeeStatus;
    }
}
