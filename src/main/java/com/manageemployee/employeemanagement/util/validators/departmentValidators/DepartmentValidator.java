package com.manageemployee.employeemanagement.util.validators.departmentValidators;

import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.util.validators.AbstractValidator;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Validator for DepartmentDTO
 */
@Component
public class DepartmentValidator extends AbstractValidator<DepartmentDTO> {

    public DepartmentValidator(@ValidatorQualifier(validatorKey = "departmentSubValidator")
                               List<Validator> subValidators) {
        super(subValidators, DepartmentDTO.class);
    }

}
