package com.manageemployee.employeemanagement.employee.model.event;


import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Getter;

@Getter
public class EmployeeUpdated extends EmployeeBaseEvent{
    private final Position oldPosition;

    public EmployeeUpdated(Employee employee, Money oldSalary, Money newSalary, Position oldPosition) {
        super(employee, oldSalary, newSalary);
        this.oldPosition = oldPosition;
    }
}
