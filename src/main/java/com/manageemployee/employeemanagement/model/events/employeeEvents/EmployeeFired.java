package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;

public class EmployeeFired extends EmployeeBaseEvent{
    public EmployeeFired(Employee employee, Money oldSalary, Money newSalary) {
        super(employee, oldSalary, newSalary);
    }
}
