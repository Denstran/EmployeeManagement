package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByCompanyBranch_Id(Long companyBranchId);

    boolean existsByPhoneNumberAndCompanyBranch_Id(String phoneNumber, Long branchId);
    boolean existsByDepartmentNameAndCompanyBranch_Id(String departmentName, Long branchId);

    Optional<Department> findDepartmentByPhoneNumberAndCompanyBranch_Id(String phoneNumber, Long branchId);
    Optional<Department> findDepartmentByDepartmentNameAndCompanyBranch_Id(String depName, Long branchId);
}
