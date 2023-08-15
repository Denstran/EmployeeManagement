package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Long> {
    CompanyBranch findCompanyBranchByCompanyBranchAddress(Address address);
    List<CompanyBranch> findCompanyBranchByCompanyBranchAddress_City(String city);
}
