package com.manageemployee.employeemanagement.companyBranch.validation;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@ValidatorQualifier(validatorKey = "companyBranchSubValidator")
public class CompanyBranchNewEntryValidator implements Validator {
    private final CompanyBranchService companyBranchService;

    @Autowired
    public CompanyBranchNewEntryValidator(CompanyBranchService companyBranchService) {
        this.companyBranchService = companyBranchService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyBranchDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyBranchDTO dto = (CompanyBranchDTO) target;
        if (dto.getId() != null) return;
        if (companyBranchService.existsByAddress(dto.getCompanyBranchAddress()))
            errors.rejectValue("companyBranchAddress", "", "Филиал с таким адресом уже существует!");

        if (companyBranchService.existsByPhoneNumber(dto.getPhoneNumber()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }
}
