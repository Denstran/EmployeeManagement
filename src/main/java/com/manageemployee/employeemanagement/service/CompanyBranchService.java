package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.exception.ResourceAlreadyExistsException;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
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
        if (getCompanyBranchByPhoneNumber(companyBranch.getPhoneNumber()) != null) {
            throw new ResourceAlreadyExistsException(String.format("Филиал с номером телефона %s уже существует",
                    companyBranch.getPhoneNumber()));
        }
        if (getCompanyBranchByAddress(companyBranch.getCompanyBranchAddress()) != null) {
            throw new ResourceAlreadyExistsException("Филиал по такому адрессу уже существует!");
        }

        return  companyBranchRepository.saveAndFlush(companyBranch);
    }

    public CompanyBranch getCompanyBranchByAddress(Address address) {
        return companyBranchRepository.findCompanyBranchByCompanyBranchAddress(address);
    }

    public CompanyBranch getCompanyBranchById(Long id) {
        return companyBranchRepository.findById(id).orElse(null);
    }

    public CompanyBranch getCompanyBranchByPhoneNumber(String phoneNumber) {
        return companyBranchRepository.findCompanyBranchByPhoneNumber(phoneNumber);
    }

    public List<CompanyBranch> getAllCompanyBranches(){
        return companyBranchRepository.findAll();
    }

    public void deleteCompanyBranchById(Long id) {
        companyBranchRepository.deleteById(id);
    }
}
