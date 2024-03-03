package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.CompanyBranchPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CompanyBranchPaymentLogRepository extends JpaRepository<CompanyBranchPaymentLog, Long>,
        JpaSpecificationExecutor<CompanyBranchPaymentLog> {
    List<CompanyBranchPaymentLog> findCompanyBranchPaymentLogByCompanyBranch_Id(Long companyBranchId);
    void deleteAllByCompanyBranch(CompanyBranch companyBranch);
}
