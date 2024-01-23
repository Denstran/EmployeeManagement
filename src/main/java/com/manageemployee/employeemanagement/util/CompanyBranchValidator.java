package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for CompanyBranchDTO
 */
@Component
public class CompanyBranchValidator implements Validator {
    private final CompanyBranchNewEntryValidator newEntryValidator;
    private final CompanyBranchUpdatingEntryValidation updatingEntryValidation;

    @Autowired
    public CompanyBranchValidator(CompanyBranchNewEntryValidator newEntryValidator,
                                  CompanyBranchUpdatingEntryValidation updatingEntryValidation) {
        this.newEntryValidator = newEntryValidator;
        this.updatingEntryValidation = updatingEntryValidation;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyBranchDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyBranchDTO companyBranchDTO = (CompanyBranchDTO) target;

        if (companyBranchDTO.getId() == null) {
           newEntryValidator.validate(companyBranchDTO, errors);
        }else {
            updatingEntryValidation.validate(companyBranchDTO, errors);
        }
    }
}
