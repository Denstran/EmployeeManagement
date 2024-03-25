package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.EmployeePaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeePaymentLogRepository extends JpaRepository<EmployeePaymentLog, Long>,
        JpaSpecificationExecutor<EmployeePaymentLog> {
}
