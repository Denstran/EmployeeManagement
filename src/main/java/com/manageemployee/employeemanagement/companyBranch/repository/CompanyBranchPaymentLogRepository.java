package com.manageemployee.employeemanagement.companyBranch.repository;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyBranchPaymentLogRepository extends JpaRepository<CompanyBranchPaymentLog, Long>,
        JpaSpecificationExecutor<CompanyBranchPaymentLog> {
}
