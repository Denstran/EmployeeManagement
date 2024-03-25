package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;

public class EmployeeHired extends EmployeeBaseEvent {
    public EmployeeHired(Employee employee, Money oldSalary, Money newSalary) {
        super(employee, oldSalary, newSalary);
    }
}
