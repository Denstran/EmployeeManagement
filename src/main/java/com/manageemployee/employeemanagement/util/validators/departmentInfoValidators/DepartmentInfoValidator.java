package com.manageemployee.employeemanagement.util.validators.departmentInfoValidators;

import com.manageemployee.employeemanagement.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.util.validators.AbstractValidator;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class DepartmentInfoValidator extends AbstractValidator<DepartmentInfoDTO> {

    @Autowired
    public DepartmentInfoValidator(@ValidatorQualifier(validatorKey = "departmentInfoSubValidator")
                                       List<Validator> subValidators) {
        super(subValidators, DepartmentInfoDTO.class);
    }
}
