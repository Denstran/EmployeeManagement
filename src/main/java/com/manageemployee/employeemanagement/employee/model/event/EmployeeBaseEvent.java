package com.manageemployee.employeemanagement.employee.model.event;

import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;

@Data
public abstract class EmployeeBaseEvent {
    private final Employee employee;
    private final Money oldSalary;
    private final Money newSalary;
}
