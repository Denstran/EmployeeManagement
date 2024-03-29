package com.manageemployee.employeemanagement.employee.model.event;

import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.util.Money;

public class EmployeeHired extends EmployeeBaseEvent {
    public EmployeeHired(Employee employee, Money oldSalary, Money newSalary) {
        super(employee, oldSalary, newSalary);
    }
}
