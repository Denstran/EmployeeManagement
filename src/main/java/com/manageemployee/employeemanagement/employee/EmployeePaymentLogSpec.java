package com.manageemployee.employeemanagement.employee;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.employee.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.position.model.Position;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class EmployeePaymentLogSpec {
    public static Specification<EmployeePaymentLog> isEqualToEmployeeId(Long employeeId) {
        return ((root, query, criteriaBuilder) -> {
            Join<EmployeePaymentLog, Employee> employeeJoin = root.join("employee");
            return criteriaBuilder.equal(employeeJoin.get("id"), employeeId);
        });
    }

    public static Specification<EmployeePaymentLog> isPhoneNumberEqualTo(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            Join<EmployeePaymentLog, Employee> employeeJoin = root.join("employee");
            return criteriaBuilder.equal(employeeJoin.get("phoneNumber"), phoneNumber);
        };
    }

    public static Specification<EmployeePaymentLog> isEqualToCompanyBranchIdAndDepartmentId(Long companyBranchId,
                                                                                            Long departmentId) {
        return ((root, query, criteriaBuilder) -> {
            Join<EmployeePaymentLog, Employee> employeeJoin = root.join("employee");
            Join<Employee, CompanyBranch> companyBranchJoin = employeeJoin.join("companyBranch");
            Join<Employee, Position> positionJoin = employeeJoin.join("position");
            Join<Position, Department> departmentJoin = positionJoin.join("department");

            Predicate isEqualToCompanyBranchId = criteriaBuilder.equal(companyBranchJoin.get("id"), companyBranchId);
            Predicate isEqualToDepartmentId = criteriaBuilder.equal(departmentJoin.get("id"), departmentId);

            return criteriaBuilder.and(isEqualToCompanyBranchId, isEqualToDepartmentId);
        });
    }

    public static Specification<EmployeePaymentLog> setupSpecification(Long employee) {
        return Specification.where(isEqualToEmployeeId(employee));
    }

    public static Specification<EmployeePaymentLog> setupSpecification(Long companyBranchId, Long departmentId) {
        return Specification.where(isEqualToCompanyBranchIdAndDepartmentId(companyBranchId, departmentId));
    }
}
