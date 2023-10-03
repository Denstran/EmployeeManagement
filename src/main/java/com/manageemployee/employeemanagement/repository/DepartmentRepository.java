package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByCompanyBranch_Id(Long companyBranchId);

    @Query("SELECT d FROM Department d " +
            "WHERE d.companyBranch.id = :companyBranchId " +
            "AND (d.departmentName = :depName OR d.phoneNumber = :phoneNumber)")
    Optional<Department> findDepByNameOrPhoneAndCompanyBranch(@Param("companyBranchId") Long companyBranchId,
                                                              @Param("depName") String depName,
                                                              @Param("phoneNumber") String phoneNumber);
}
