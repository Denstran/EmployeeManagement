package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.EmployeeStatus;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Long> {
    EmployeeStatus findEmployeeStatusByEmployeeStatus(EEmployeeStatus employeeStatus);
}
