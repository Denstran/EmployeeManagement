package com.manageemployee.employeemanagement.employee.validation.vacation;

import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.util.validators.AbstractValidator;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class VacationValidator extends AbstractValidator<VacationRequestDTO> {

    public VacationValidator(@ValidatorQualifier(validatorKey = "vacationSubValidator") List<Validator> subValidators) {
        super(subValidators, VacationRequestDTO.class);
    }
}
