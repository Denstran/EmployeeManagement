package com.manageemployee.employeemanagement.util.validators.companyBranchValidators;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.util.validators.markers.CompanyBranchDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class CompanyBranchNewEntryValidator implements CompanyBranchDTOValidator {
    private final CompanyBranchService companyBranchService;

    @Autowired
    public CompanyBranchNewEntryValidator(CompanyBranchService companyBranchService) {
        this.companyBranchService = companyBranchService;
    }

    @Override
    public void validate(CompanyBranchDTO target, Errors errors) {
        if (target.getId() != null) return;
        if (companyBranchService.existsByAddress(target.getCompanyBranchAddress()))
            errors.rejectValue("companyBranchAddress", "", "Филиал с таким адресом уже существует!");

        if (companyBranchService.existsByPhoneNumber(target.getPhoneNumber()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }
}
