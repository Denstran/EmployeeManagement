package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Long> {
    CompanyBranch findCompanyBranchByPhoneNumber(String phoneNumber);
    CompanyBranch findCompanyBranchByCompanyBranchAddress(Address address);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCompanyBranchAddress(Address address);
}
