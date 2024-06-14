package com.manageemployee.employeemanagement.employee.service.eventPublisher;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import org.springframework.stereotype.Component;

@Component
public class EmployeePositionChangedEventPublisher implements EmployeeEventPublisher {
    @Override
    public void publishEvent(Employee employee, Employee employeeFromDB) {
        if (isSamePosition(employee, employeeFromDB)) return;
        if (isFired(employee)) return;

        employee.changePosition(employeeFromDB.getPosition());
    }

    private boolean isSamePosition(Employee employee, Employee employeeFromDB) {
        return employee.getPosition().getId().equals(employeeFromDB.getPosition().getId());
    }

    private boolean isFired(Employee employee) {
        return EmployeeStatus.FIRED.equals(employee.getEmployeeStatus());
    }
}
