package com.manageemployee.employeemanagement.employee.model.event.employeeEvent;


import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class EmployeeUpdated extends EmployeeBaseEvent{
    private final Position oldPosition;

    public EmployeeUpdated(Employee employee, Money oldSalary, Money newSalary, Position oldPosition) {
        super(employee, oldSalary, newSalary);
        log.info("Old salary: {}, New salary: {}", oldSalary, newSalary);
        this.oldPosition = oldPosition;
    }
}
