package com.manageemployee.employeemanagement.employee.service.eventPublisher;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import com.manageemployee.employeemanagement.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeSalaryChangedEventPublisher implements EmployeeEventPublisher {
    @Override
    public void publishEvent(Employee employee, Employee employeeFromDB) {
        if (isFired(employee)) return;

        Money newSalary = employee.getSalary();
        Money oldSalary = employeeFromDB.getSalary();
        log.info("New salary: {} Old salary: {}", newSalary, oldSalary);
        if (AreSalariesEqual(newSalary, oldSalary)) return;

        employee.changeSalary(new Money(oldSalary), employeeFromDB.getPosition().getDepartment());
    }

    private boolean isFired(Employee employee) {
        return EmployeeStatus.FIRED.equals(employee.getEmployeeStatus());
    }

    private boolean AreSalariesEqual(Money newSalary, Money oldSalary) {
        log.info("Equality result: {}", newSalary.equals(oldSalary));
        return newSalary.equals(oldSalary);
    }
}
