package com.manageemployee.employeemanagement.employee.service.eventPublisher;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeDepartmentChangedEventPublisher implements EmployeeEventPublisher {
    @Override
    public void publishEvent(Employee employee, Employee employeeFromDB) {
        log.info("PROCESSING DEPARTMENT CHANGE");
        if (isFired(employee)) return;
        if (isSameDepartment(employee, employeeFromDB)) return;

        employee.changeDepartment(employeeFromDB.getPosition().getDepartment());
    }

    private boolean isFired(Employee employee) {
        log.info("EMPLOYEE STATUS: {}", employee.getEmployeeStatus());
        return EmployeeStatus.FIRED.equals(employee.getEmployeeStatus());
    }

    private boolean isSameDepartment(Employee employee, Employee employeeFromDB) {
        log.info("Old department id: {}, New department id: {}",
                employeeFromDB.getPosition().getDepartment().getId(),
                employee.getPosition().getDepartment().getId());

        return employee.getPosition().getDepartment().getId()
                .equals(employeeFromDB.getPosition().getDepartment().getId());
    }
}
