package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Optional<Employee> findEmployeeByPhoneNumber(String phoneNumber);
    Optional<Employee> findEmployeeByEmail(String email);
}
