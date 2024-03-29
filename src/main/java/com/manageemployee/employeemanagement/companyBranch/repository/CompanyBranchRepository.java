package com.manageemployee.employeemanagement.companyBranch.repository;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.util.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Long> {

    Optional<CompanyBranch> findCompanyBranchByPhoneNumber(String phoneNumber);
    Optional<CompanyBranch> findCompanyBranchByCompanyBranchAddress(Address address);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCompanyBranchAddress(Address address);

    @Query("SELECT di FROM DepartmentInfo di WHERE di.pk.companyBranch.id = :companyBranchId")
    List<DepartmentInfo> getDepartments(@Param("companyBranchId") Long companyBranchId);
}
