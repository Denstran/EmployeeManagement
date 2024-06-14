package com.manageemployee.employeemanagement.employee.service.eventPublisher;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import org.springframework.stereotype.Component;

@Component
public class EmployeeRestoredEventPublisher implements EmployeeEventPublisher {
    @Override
    public void publishEvent(Employee employee, Employee employeeFromDB) {
        if (!isRestoringEmployee(employee, employeeFromDB)) return;

        employee.restore();
    }

    private boolean isRestoringEmployee(Employee employee, Employee employeeFromDB) {
        return EmployeeStatus.FIRED.equals(employeeFromDB.getEmployeeStatus()) &&
                !EmployeeStatus.FIRED.equals(employee.getEmployeeStatus());
    }
}
