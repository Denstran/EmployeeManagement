package com.manageemployee.employeemanagement.util.validators.positionValidators;

import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.util.validators.AbstractValidator;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class PositionValidator extends AbstractValidator<PositionDTO> {
    public PositionValidator(@ValidatorQualifier(validatorKey = "positionSubValidator") List<Validator> subValidators) {
        super(subValidators, PositionDTO.class);
    }
}
