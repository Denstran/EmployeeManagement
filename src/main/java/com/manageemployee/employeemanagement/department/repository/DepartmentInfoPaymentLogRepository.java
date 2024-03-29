package com.manageemployee.employeemanagement.department.repository;

import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentInfoPaymentLogRepository extends JpaRepository<DepartmentInfoPaymentLog, Long>,
        JpaSpecificationExecutor<DepartmentInfoPaymentLog> {
}