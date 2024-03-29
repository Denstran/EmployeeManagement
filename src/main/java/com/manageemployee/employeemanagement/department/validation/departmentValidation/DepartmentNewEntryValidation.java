package com.manageemployee.employeemanagement.department.validation.departmentValidation;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@ValidatorQualifier(validatorKey = "departmentSubValidator")
public class DepartmentNewEntryValidation implements Validator {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentNewEntryValidation(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepartmentDTO dto = (DepartmentDTO) target;
        if (dto.getId() != null) return;

        if (departmentService.existsByName(dto.getDepartmentName()))
            errors.rejectValue("departmentName", "", "Отдел с таким название уже существует!");
    }
}
