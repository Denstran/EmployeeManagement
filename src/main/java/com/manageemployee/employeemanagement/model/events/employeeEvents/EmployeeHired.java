package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Getter;

@Getter
public class EmployeeHired extends EmployeeBaseEvent {
    private final Employee employee;
    public EmployeeHired(Money salary, Department department, CompanyBranch companyBranch, Employee employee) {
        super(salary, department, companyBranch);
        this.employee = employee;
    }
}
