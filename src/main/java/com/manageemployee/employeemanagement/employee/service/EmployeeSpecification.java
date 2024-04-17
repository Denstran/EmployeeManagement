package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.employee.model.EmployeeStatus;
import com.manageemployee.employeemanagement.employee.model.Employee_;
import com.manageemployee.employeemanagement.position.model.Position;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class EmployeeSpecification {
    public static Specification<Employee> isInCompanyBranchAndDepartment(Long companyBranchId, Long departmentId) {
        return (root, query, criteriaBuilder) -> {
            Join<Employee, CompanyBranch> companyBranchJoin = root.join(Employee_.companyBranch);
            Join<Employee, Position> positionJoin = root.join(Employee_.position);
            Join<Position, Department> departmentJoin = positionJoin.join("department");

            Predicate isEqualToDepartmentId = criteriaBuilder.equal(departmentJoin.get("id"), departmentId);
            Predicate isEqualToCompanyBranchId = criteriaBuilder.equal(companyBranchJoin.get("id"), companyBranchId);

            return criteriaBuilder.and(isEqualToCompanyBranchId, isEqualToDepartmentId);
        };
    }

    public static Specification<Employee> setupSpecification(Long companyBranchId, Long departmentId) {
        return Specification.where(isInCompanyBranchAndDepartment(companyBranchId, departmentId));
    }

    public static Specification<Employee> isEqualToEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get(Employee_.email), email));
    }

    public static Specification<Employee> isEqualToPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get(Employee_.phoneNumber), phoneNumber));
    }

    public static Specification<Employee> isEqualToStatus(EmployeeStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get(Employee_.employeeStatus), status));
    }

    public static Specification<Employee> isSalaryBetween(double salaryStart, double salaryEnd) {
        return (root, query, criteriaBuilder) -> {
            Expression<Double> salaryAmount = root.get(Employee_.salary).get("amount");
            Predicate isSalaryBetween = criteriaBuilder.between(salaryAmount, salaryStart, salaryEnd);
            return criteriaBuilder.and(isSalaryBetween);
        };
    }

    public static Specification<Employee> isEqualToPosition(String positionName) {
        return (root, query, criteriaBuilder) -> {
            log.info("FROM EmployeeSpecification: POSITION FILTER WITH POSITION NAME: {}", positionName);
            Expression<String> employeePosition = root.get(Employee_.position).get("positionName");
            Predicate isEqualToPosition = criteriaBuilder.equal(employeePosition, positionName);
            return criteriaBuilder.and(isEqualToPosition);
        };
    }

    public static Specification<Employee> isInWorkingYearsRange(int startAmountOfWorkingYears,
                                                                int endAmountOfWorkingYears) {
        return (root, query, criteriaBuilder) -> {
            Expression<Integer> yearOfEmployment = criteriaBuilder.function(
                    "YEAR", Integer.class, root.get(Employee_.employmentDate));

            Expression<Integer> currentYear = criteriaBuilder.function(
                    "YEAR", Integer.class, criteriaBuilder.currentTimestamp());

            Expression<Integer> yearsOfWorking = criteriaBuilder.diff(currentYear, yearOfEmployment);

            Predicate isInWorkingYearsRange = criteriaBuilder.between(yearsOfWorking,
                                                                      startAmountOfWorkingYears,
                                                                      endAmountOfWorkingYears);

            return criteriaBuilder.and(isInWorkingYearsRange);
        };
    }
}
