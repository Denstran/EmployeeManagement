package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.repository.CompanyBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyBranchService {
    private final CompanyBranchRepository companyBranchRepository;

    @Autowired
    public CompanyBranchService(CompanyBranchRepository companyBranchRepository) {
        this.companyBranchRepository = companyBranchRepository;
    }

    public CompanyBranch createCompanyBranch(CompanyBranch companyBranch) {

        return  companyBranchRepository.saveAndFlush(companyBranch);
    }

    public CompanyBranch updateCompanyBranch(CompanyBranch companyBranch) {

        return companyBranchRepository.saveAndFlush(companyBranch);
    }

    public CompanyBranch getCompanyBranchById(Long id) {
        return companyBranchRepository.findById(id).orElse(null);
    }


    public List<CompanyBranch> getAllCompanyBranches(){
        return companyBranchRepository.findAll();
    }

    public void removeDepartment(CompanyBranch companyBranch, Department department) {
        companyBranch.removeDepartment(department);
        companyBranchRepository.saveAndFlush(companyBranch);
    }

    public void addDepartment(CompanyBranch companyBranch, Department department) {
        companyBranch.addDepartment(department);
        companyBranchRepository.saveAndFlush(companyBranch);
    }

    public void deleteCompanyBranchById(Long id) {
        companyBranchRepository.deleteById(id);
    }
}
