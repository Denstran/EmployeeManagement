package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

/**
 * Validator for CompanyBranchDTO
 */
@Component
public class CompanyBranchValidator extends BasicEntryValidation<CompanyBranchDTO> implements Validator {

    private final CompanyBranchService companyBranchService;

    @Autowired
    public CompanyBranchValidator(CompanyBranchService companyBranchService) {
        this.companyBranchService = companyBranchService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyBranchDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyBranchDTO companyBranchDTO = (CompanyBranchDTO) target;

        if (companyBranchDTO.getId() == null) {
           validateNewEntry(companyBranchDTO, errors);
        }else {
            validateUpdatingEntry(companyBranchDTO, errors);
        }
    }

    @Override
    protected void validateNewEntry(CompanyBranchDTO companyBranchDTO, Errors errors) {
        if (companyBranchService.existsByAddress(companyBranchDTO.getCompanyBranchAddress()))
            errors.rejectValue("companyBranchAddress", "", "Филиал с таким адресом уже существует!");

        if (companyBranchService.existsByPhoneNumber(companyBranchDTO.getPhoneNumber()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }

    @Override
    protected void validateUpdatingEntry(CompanyBranchDTO companyBranchDTO, Errors errors) {
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
