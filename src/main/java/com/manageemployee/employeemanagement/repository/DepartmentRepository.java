package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByCompanyBranch_Id(Long companyBranchId);

    @Query("SELECT c FROM CompanyBranch c " +
            "JOIN Department d " +
            "ON c.id = d.companyBranch.id " +
            "WHERE d.id = :depId")
    Optional<CompanyBranch> findCompanyBranchByDepartmentId(@Param("depId") Long depId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE Employee e WHERE e.department = :department")
    void deleteAllEmployeesByDepartment(@Param("department") Department department);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE Position p WHERE p.department = :department")
    void deleteAllPositionsByDepartment(@Param("department") Department department);

    boolean existsByPhoneNumberAndCompanyBranch_Id(String phoneNumber, Long branchId);
    boolean existsByDepartmentNameAndCompanyBranch_Id(String departmentName, Long branchId);

    Optional<Department> findDepartmentByPhoneNumberAndCompanyBranch_Id(String phoneNumber, Long branchId);
    Optional<Department> findDepartmentByDepartmentNameAndCompanyBranch_Id(String depName, Long branchId);
}
