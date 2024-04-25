package com.manageemployee.employeemanagement.employee.model.event.employeeEvent;

import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.util.Money;

public class EmployeeFired extends EmployeeBaseEvent{
    public EmployeeFired(Employee employee, Money oldSalary, Money newSalary) {
        super(employee, oldSalary, newSalary);
    }
}
