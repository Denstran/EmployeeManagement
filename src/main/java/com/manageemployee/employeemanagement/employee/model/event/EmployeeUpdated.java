package com.manageemployee.employeemanagement.employee.model.event;


import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.util.Money;

public class EmployeeUpdated extends EmployeeBaseEvent{

    public EmployeeUpdated(Employee employee, Money oldSalary, Money newSalary) {
        super(employee, oldSalary, newSalary);
    }
}
