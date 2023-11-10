package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Long> {
    CompanyBranch findCompanyBranchByPhoneNumber(String phoneNumber);
    CompanyBranch findCompanyBranchByCompanyBranchAddress(Address address);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCompanyBranchAddress(Address address);

    @Query("SELECT c FROM CompanyBranch c " +
            "JOIN c.departments d " +
            "ON c.id = d.companyBranch.id " +
            "WHERE d.id = :depId")
    Optional<CompanyBranch> findCompanyBranchByDepartmentId(@Param("depId") Long depId);
}
