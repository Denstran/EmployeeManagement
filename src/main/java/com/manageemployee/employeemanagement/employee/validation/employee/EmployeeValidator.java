package com.manageemployee.employeemanagement.employee.validation.employee;

import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.util.validators.AbstractValidator;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class EmployeeValidator extends AbstractValidator<EmployeeDTO> {
    public EmployeeValidator(@ValidatorQualifier(validatorKey = "employeeSubValidator") List<Validator> subValidators) {
        super(subValidators, EmployeeDTO.class);
    }
}