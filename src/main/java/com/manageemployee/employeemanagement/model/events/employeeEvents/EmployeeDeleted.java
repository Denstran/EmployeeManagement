package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.*;
import lombok.Getter;


@Getter
public class EmployeeDeleted extends EmployeeBaseEvent {

    private final EmployeeStatus employeeStatus;
    private final Employee employee;
    public EmployeeDeleted(Money salary, Department department, CompanyBranch companyBranch,
                           EmployeeStatus employeeStatus, Employee employee) {
        super(salary, department, companyBranch);
        this.employeeStatus = employeeStatus;
        this.employee = employee;
    }
}
