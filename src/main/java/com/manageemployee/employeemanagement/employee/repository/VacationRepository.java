package com.manageemployee.employeemanagement.employee.repository;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationRepository extends JpaRepository<VacationRequest, Long> {
    List<VacationRequest> findByEmployee(Employee employee);
}
