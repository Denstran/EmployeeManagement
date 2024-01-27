package com.manageemployee.employeemanagement.util.validators.companyBranchValidators;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.util.validators.markers.CompanyBranchDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Objects;
import java.util.Optional;

@Service
public class CompanyBranchUpdatingEntryValidation implements CompanyBranchDTOValidator {
    private final CompanyBranchService companyBranchService;

    @Autowired
    public CompanyBranchUpdatingEntryValidation(CompanyBranchService companyBranchService) {
        this.companyBranchService = companyBranchService;
    }

    @Override
    public void validate(CompanyBranchDTO target, Errors errors) {
        if (target.getId() == null) return;

        Optional<CompanyBranch> companyBranchByAddress = companyBranchService
                .findByAddress(target.getCompanyBranchAddress());

        Optional<CompanyBranch> companyBranchByPhone = companyBranchService
                .findByPhoneNumber(target.getPhoneNumber());

        if (companyBranchByAddress.isPresent() && !Objects.equals(companyBranchByAddress.get().getId(),
                target.getId()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");

        if (companyBranchByPhone.isPresent() && !Objects.equals(companyBranchByPhone.get().getId(),
                target.getId()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }

}
