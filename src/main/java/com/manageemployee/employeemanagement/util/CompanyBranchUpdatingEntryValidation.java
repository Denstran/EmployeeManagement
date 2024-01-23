package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Service
public class CompanyBranchUpdatingEntryValidation implements Validator {
    private final CompanyBranchService companyBranchService;

    public CompanyBranchUpdatingEntryValidation(CompanyBranchService companyBranchService) {
        this.companyBranchService = companyBranchService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyBranchDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyBranchDTO companyBranchDTO = (CompanyBranchDTO) target;

        Optional<CompanyBranch> companyBranchByAddress = companyBranchService
                .findByAddress(companyBranchDTO.getCompanyBranchAddress());

        Optional<CompanyBranch> companyBranchByPhone = companyBranchService
                .findByPhoneNumber(companyBranchDTO.getPhoneNumber());

        if (companyBranchByAddress.isPresent() && !Objects.equals(companyBranchByAddress.get().getId(),
                companyBranchDTO.getId()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");

        if (companyBranchByPhone.isPresent() && !Objects.equals(companyBranchByPhone.get().getId(),
                companyBranchDTO.getId()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }
}
