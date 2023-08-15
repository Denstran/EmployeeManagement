package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Long> {
}
