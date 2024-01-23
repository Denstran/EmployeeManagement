package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Long> {

    Optional<CompanyBranch> findCompanyBranchByPhoneNumber(String phoneNumber);
    Optional<CompanyBranch> findCompanyBranchByCompanyBranchAddress(Address address);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCompanyBranchAddress(Address address);
}
