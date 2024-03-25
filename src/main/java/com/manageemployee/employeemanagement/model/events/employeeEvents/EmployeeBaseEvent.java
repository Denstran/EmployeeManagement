package com.manageemployee.employeemanagement.model.events.employeeEvents;

import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public abstract class EmployeeBaseEvent {
    private final Employee employee;
    private final Money oldSalary;
    private final Money newSalary;
}
