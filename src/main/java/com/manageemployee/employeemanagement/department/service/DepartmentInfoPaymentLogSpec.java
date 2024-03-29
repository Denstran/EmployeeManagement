package com.manageemployee.employeemanagement.department.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentInfoPaymentLogSpec {

    public static Specification<DepartmentInfoPaymentLog> isEqualToCompanyBranchIdAndDepartmentId(Long companyBranchId,
                                                                                           Long departmentId) {
        return ((root, query, criteriaBuilder) -> {
            Join<DepartmentInfoPaymentLog, Department> departmentJoin = root.join("department");
            Join<CompanyBranchPaymentLog, CompanyBranch> companyBranchJoin = root.join("companyBranch");
            Predicate isEqualToDepartmentId = criteriaBuilder.equal(departmentJoin.get("id"), departmentId);
            Predicate isEqualToCompanyBranchId = criteriaBuilder.equal(companyBranchJoin.get("id"), companyBranchId);

            return criteriaBuilder.and(isEqualToCompanyBranchId, isEqualToDepartmentId);
        });
    }

    public static Specification<DepartmentInfoPaymentLog> setupSpecification(Long companyBranchId, Long departmentId) {
        return Specification.where(isEqualToCompanyBranchIdAndDepartmentId(companyBranchId, departmentId));
    }
}
