package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    void deleteAllByDepartment(Department department);

    @Modifying
    @Query(value = "DELETE FROM Employee e WHERE e.department.id IN (SELECT d.id from Department d where d.companyBranch.id = :companyBranchId)")
    void deleteAllByCompanyBranchId(@Param("companyBranchId") Long companyBranchId);
    List<Employee> findAllByDepartment_Id(Long depId);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Optional<Employee> findEmployeeByPhoneNumber(String phoneNumber);
    Optional<Employee> findEmployeeByEmail(String email);
}
