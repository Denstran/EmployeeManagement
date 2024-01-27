package com.manageemployee.employeemanagement.util.validators.companyBranchValidators;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.util.validators.ValidatorHandler;
import com.manageemployee.employeemanagement.util.validators.markers.CompanyBranchDTOValidator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Validator for CompanyBranchDTO
 */
@Component
public class CompanyBranchValidator extends ValidatorHandler<CompanyBranchDTO, CompanyBranchDTOValidator> {

    public CompanyBranchValidator(List<CompanyBranchDTOValidator> validators) {
        super(validators);
    }
}
