package com.manageemployee.employeemanagement.employee.model.event.employeeEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class EmployeeBaseEvent {
    private final Employee employee;
    private final Money oldSalary;
    private final Money newSalary;

    public EmployeeBaseEvent(Employee employee, Money oldSalary, Money newSalary) {
        this.employee = employee;
        this.oldSalary = oldSalary;
        this.newSalary = newSalary;

        log.info("Old salary: {}, New salary: {}", oldSalary, newSalary);
    }
}
