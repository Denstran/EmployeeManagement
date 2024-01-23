package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class CompanyBranchNewEntryValidator implements Validator {
    private final CompanyBranchService companyBranchService;

    public CompanyBranchNewEntryValidator(CompanyBranchService companyBranchService) {
        this.companyBranchService = companyBranchService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyBranchDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyBranchDTO companyBranchDTO = (CompanyBranchDTO) target;
        if (companyBranchService.existsByAddress(companyBranchDTO.getCompanyBranchAddress()))
            errors.rejectValue("companyBranchAddress", "", "Филиал с таким адресом уже существует!");

        if (companyBranchService.existsByPhoneNumber(companyBranchDTO.getPhoneNumber()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }
}
