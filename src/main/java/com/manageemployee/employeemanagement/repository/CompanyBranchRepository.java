package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Long> {

    Optional<CompanyBranch> findCompanyBranchByPhoneNumber(String phoneNumber);
    Optional<CompanyBranch> findCompanyBranchByCompanyBranchAddress(Address address);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCompanyBranchAddress(Address address);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE Department d WHERE d.companyBranch.id = :companyBranchId")
    void deleteAllDepartmentsByCompanyBranch_Id(@Param("companyBranchId") Long companyBranchId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE Employee e WHERE e.department.id IN (SELECT d.id from Department d where d.companyBranch.id = :companyBranchId)")
    void deleteAllEmployeesByCompanyBranchId(@Param("companyBranchId") Long companyBranchId);
}
