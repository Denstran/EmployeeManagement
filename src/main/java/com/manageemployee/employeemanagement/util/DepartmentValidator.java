package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.repository.DepartmentRepository;
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
public class DepartmentValidator extends BasicEntryValidation<DepartmentDTO>  implements Validator {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentValidator(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepartmentDTO departmentDTO = (DepartmentDTO) target;

        if (departmentDTO.getId() == null) {
            validateNewEntry(departmentDTO, errors);
        } else {
            validateUpdatingEntry(departmentDTO, errors);
        }
    }

    @Override
    protected void validateNewEntry(DepartmentDTO departmentDTO, Errors errors) {
        if (departmentRepository.existsByDepartmentNameAndCompanyBranch_Id(departmentDTO.getDepartmentName(),
                departmentDTO.getCompanyBranchId()))
            errors.rejectValue("departmentName", "", "Отдел с таким названием уже существует!");

        if (departmentRepository.existsByPhoneNumberAndCompanyBranch_Id(departmentDTO.getPhoneNumber(),
                departmentDTO.getCompanyBranchId()))
            errors.rejectValue("phoneNumber", "", "Отдел с такими телефоном уже существует!");
    }

    @Override
    protected void validateUpdatingEntry(DepartmentDTO departmentDTO, Errors errors) {
        Optional<Department> departmentByPhone = departmentRepository.findDepartmentByPhoneNumberAndCompanyBranch_Id(
                departmentDTO.getPhoneNumber(), departmentDTO.getCompanyBranchId()
        );
        Optional<Department> departmentByName = departmentRepository
                .findDepartmentByDepartmentNameAndCompanyBranch_Id(
                        departmentDTO.getDepartmentName(), departmentDTO.getCompanyBranchId()
                );

        if (departmentByName.isPresent() && !Objects.equals(departmentByName.get().getId(), departmentDTO.getId()))
            errors.rejectValue("departmentName", "", "Отдел с таким названием уже существует!");

        if (departmentByPhone.isPresent() && !Objects.equals(departmentByPhone.get().getId(), departmentDTO.getId()))
            errors.rejectValue("phoneNumber", "", "Отдел с такими телефоном уже существует!");
    }
}
