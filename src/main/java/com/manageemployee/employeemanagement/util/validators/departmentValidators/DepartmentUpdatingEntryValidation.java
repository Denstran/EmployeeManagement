package com.manageemployee.employeemanagement.util.validators.departmentValidators;

import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@ValidatorQualifier(validatorKey = "departmentSubValidator")
public class DepartmentUpdatingEntryValidation implements Validator {

    private final DepartmentService departmentService;

    public DepartmentUpdatingEntryValidation(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepartmentDTO dto = (DepartmentDTO) target;
        if (dto.getId() == null) return;

        Optional<Department> department = departmentService.findByName(dto.getDepartmentName());

        if (department.isPresent() && !department.get().getId().equals(dto.getId()))
            errors.rejectValue("departmentName", "", "Отдел с таким название уже существует!");
    }
}
