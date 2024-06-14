package com.manageemployee.employeemanagement.employee.service.eventPublisher;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;

public interface EmployeeEventPublisher {
    void publishEvent(Employee employee, Employee employeeFromDB);
}
