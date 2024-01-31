package com.manageemployee.employeemanagement.util.validators.companyBranchValidators;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
@ValidatorQualifier(validatorKey = "companyBranchSubValidator")
public class CompanyBranchUpdatingEntryValidation implements Validator {
    private final CompanyBranchService companyBranchService;

    @Autowired
    public CompanyBranchUpdatingEntryValidation(CompanyBranchService companyBranchService) {
        this.companyBranchService = companyBranchService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyBranchDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyBranchDTO dto = (CompanyBranchDTO) target;
        if (dto.getId() == null) return;

        Optional<CompanyBranch> companyBranchByAddress = companyBranchService
                .findByAddress(dto.getCompanyBranchAddress());

        Optional<CompanyBranch> companyBranchByPhone = companyBranchService
                .findByPhoneNumber(dto.getPhoneNumber());

        if (companyBranchByAddress.isPresent() && !Objects.equals(companyBranchByAddress.get().getId(),
                dto.getId()))
            errors.rejectValue("companyBranchAddress", "", "Филиал с таким адресом уже существует!");

        if (companyBranchByPhone.isPresent() && !Objects.equals(companyBranchByPhone.get().getId(),
                dto.getId()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }

}
