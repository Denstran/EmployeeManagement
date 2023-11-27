package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.repository.CompanyBranchRepository;
import com.manageemployee.employeemanagement.repository.DepartmentRepository;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyBranchService {
    private final CompanyBranchRepository companyBranchRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public CompanyBranchService(CompanyBranchRepository companyBranchRepository,
                                DepartmentRepository departmentRepository,
                                EmployeeRepository employeeRepository) {
        this.companyBranchRepository = companyBranchRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }


    @Transactional
    public CompanyBranch createCompanyBranch(CompanyBranch companyBranch) {

        return  companyBranchRepository.saveAndFlush(companyBranch);
    }

    @Transactional
    public CompanyBranch updateCompanyBranch(CompanyBranch companyBranch) {

        return companyBranchRepository.saveAndFlush(companyBranch);
    }

    public CompanyBranch getCompanyBranchById(Long id) {
        return companyBranchRepository.findById(id).orElse(null);
    }

    public CompanyBranch getCompanyBranchReferenceById(Long id) {
        return companyBranchRepository.getReferenceById(id);
    }

    public List<CompanyBranch> getAllCompanyBranches(){
        return companyBranchRepository.findAll();
    }

    @Transactional
    public void deleteCompanyBranchById(Long id) {
        employeeRepository.deleteAllByCompanyBranchId(id);
        departmentRepository.deleteAllByCompanyBranch_Id(id);
        companyBranchRepository.deleteById(id);
    }
}
