package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(String depName);
    Optional<Department> findDepartmentByDepartmentName(String depName);
}
