package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranchPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyBranchPaymentLogRepository extends JpaRepository<CompanyBranchPaymentLog, Long>,
        JpaSpecificationExecutor<CompanyBranchPaymentLog> {
}
