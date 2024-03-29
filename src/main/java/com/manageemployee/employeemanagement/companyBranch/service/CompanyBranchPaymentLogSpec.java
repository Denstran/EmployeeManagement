package com.manageemployee.employeemanagement.companyBranch.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CompanyBranchPaymentLogSpec {

    public static Specification<CompanyBranchPaymentLog> isIdEqual(Long companyBranchId) {
        return (root, query, criteriaBuilder) -> {
            Join<CompanyBranchPaymentLog, CompanyBranch> companyBranchJoin = root.join("companyBranch");
            return criteriaBuilder.equal(companyBranchJoin.get("id"), companyBranchId);
        };
    }

    public static Specification<CompanyBranchPaymentLog> setupSpecification(Long companyBranchId) {
        return Specification.where(isIdEqual(companyBranchId));
    }
}
