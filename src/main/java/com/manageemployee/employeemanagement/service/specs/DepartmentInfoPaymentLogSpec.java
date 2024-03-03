package com.manageemployee.employeemanagement.service.specs;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfoPaymentLog;
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

    public static Specification<DepartmentInfoPaymentLog> isBetweenDates(String startDate, String endDate) {
        return (Specification<DepartmentInfoPaymentLog>) AbstractPaymentLogSpec.isBetweenDates(startDate, endDate);
    }

    public static Specification<DepartmentInfoPaymentLog> isTransferActionEqualTo(String transferAction) {
        return (Specification<DepartmentInfoPaymentLog>) AbstractPaymentLogSpec.isTransferActionEqualTo(transferAction);
    }
}
