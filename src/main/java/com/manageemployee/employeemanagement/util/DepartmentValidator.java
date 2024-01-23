package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

/**
 * Validator for DepartmentDTO
 */
@Component
public class DepartmentValidator implements Validator {

    private final DepartmentService departmentService;

    public DepartmentValidator(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepartmentDTO departmentDTO = (DepartmentDTO) target;

        if (departmentDTO.getId() == null) {
            if (departmentService.existsByName(departmentDTO.getDepartmentName()))
                errors.rejectValue("departmentName", "", "Отдел с таким название уже существует!");
        }else {
            Optional<Department> department = departmentService.findByName(departmentDTO.getDepartmentName());

            if (department.isPresent() && !department.get().getId().equals(departmentDTO.getId()))
                errors.rejectValue("departmentName", "", "Отдел с таким название уже существует!");
        }
    }
}
