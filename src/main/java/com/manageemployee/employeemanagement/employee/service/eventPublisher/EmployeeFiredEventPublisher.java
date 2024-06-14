package com.manageemployee.employeemanagement.employee.service.eventPublisher;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFiredEventPublisher implements EmployeeEventPublisher {

    @Override
    public void publishEvent(Employee employee, Employee employeeFromDB) {
        if (!isFired(employee, employeeFromDB)) return;

        employee.fireEmployee(employeeFromDB.getSalary());
    }

    private boolean isFired(Employee employee, Employee employeeFromDB) {
        return employee.getEmployeeStatus().equals(EmployeeStatus.FIRED)
                && !employeeFromDB.getEmployeeStatus().equals(EmployeeStatus.FIRED);
    }
}
