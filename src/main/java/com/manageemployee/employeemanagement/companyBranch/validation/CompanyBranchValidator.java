package com.manageemployee.employeemanagement.companyBranch.validation;


import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.util.validators.AbstractValidator;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Validator for CompanyBranchDTO
 */
@Component
public class CompanyBranchValidator extends AbstractValidator<CompanyBranchDTO> {
    @Autowired
    public CompanyBranchValidator(@ValidatorQualifier(validatorKey = "companyBranchSubValidator")
                                  List<Validator> validators) {
        super(validators, CompanyBranchDTO.class);
    }

}
