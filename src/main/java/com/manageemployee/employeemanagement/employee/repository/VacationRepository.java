package com.manageemployee.employeemanagement.employee.repository;

import com.manageemployee.employeemanagement.employee.model.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<VacationRequest, Long> {
}
