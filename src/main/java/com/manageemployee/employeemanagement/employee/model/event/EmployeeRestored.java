package com.manageemployee.employeemanagement.employee.model.event;

import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.util.Money;
import jakarta.validation.constraints.NotNull;

public class EmployeeRestored extends EmployeeBaseEvent {
    public EmployeeRestored(Employee employee, @NotNull Money oldSalary, @NotNull Money newSalary) {
        super(employee, oldSalary, newSalary);
    }
}
