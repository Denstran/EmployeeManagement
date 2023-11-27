package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByCompanyBranch_Id(Long companyBranchId);

    @Modifying
    @Query(value = "DELETE Department d WHERE d.companyBranch.id = :companyBranchId")
    void deleteAllByCompanyBranch_Id(Long companyBranchId);
    @Query(value = "SELECT e FROM Employee e " +
            "JOIN Department d ON e.department.id = d.id " +
            "WHERE d.id = :depId")
    List<Employee> findAllEmployees(@Param("depId") Long depId);

    boolean existsByPhoneNumberAndCompanyBranch_Id(String phoneNumber, Long branchId);
    boolean existsByDepartmentNameAndCompanyBranch_Id(String departmentName, Long branchId);

    Optional<Department> findDepartmentByPhoneNumberAndCompanyBranch_Id(String phoneNumber, Long branchId);
    Optional<Department> findDepartmentByDepartmentNameAndCompanyBranch_Id(String depName, Long branchId);
}
