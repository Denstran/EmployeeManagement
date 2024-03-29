package com.manageemployee.employeemanagement.companyBranch.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.util.AbstractPaymentLogSpec;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CompanyBranchPaymentLogSpec {

    public static Specification<CompanyBranchPaymentLog> isIdEqual(Long companyBranchId) {
        return (root, query, criteriaBuilder) -> {
            Join<CompanyBranchPaymentLog, CompanyBranch> companyBranchJoin = root.join("companyBranch");
            return criteriaBuilder.equal(companyBranchJoin.get("id"), companyBranchId);
        };
    }
    public static Specification<CompanyBranchPaymentLog> isBetweenDates(String startDate, String endDate) {
        return (Specification<CompanyBranchPaymentLog>) AbstractPaymentLogSpec.isBetweenDates(startDate, endDate);
    }

    public static Specification<CompanyBranchPaymentLog> isTransferActionEqualTo(String transferAction) {
        return (Specification<CompanyBranchPaymentLog>) AbstractPaymentLogSpec.isTransferActionEqualTo(transferAction);
    }
}
