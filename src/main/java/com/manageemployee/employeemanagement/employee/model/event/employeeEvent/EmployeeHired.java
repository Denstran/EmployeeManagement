package com.manageemployee.employeemanagement.employee.model.event.employeeEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Getter;

@Getter
public class EmployeeHired extends EmployeeBaseEvent {
    private final String password;
    public EmployeeHired(Employee employee, Money oldSalary, Money newSalary, String password) {
        super(employee, oldSalary, newSalary);
        this.password = password;
    }
}
