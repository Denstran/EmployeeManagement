package com.manageemployee.employeemanagement.util.validators.positionValidators;

import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@ValidatorQualifier(validatorKey = "positionSubValidator")
public class PositionNewEntryValidator implements Validator {
    private final PositionService positionService;
    private final DepartmentService departmentService;

    @Autowired
    public PositionNewEntryValidator(PositionService positionService, DepartmentService departmentService) {
        this.positionService = positionService;
        this.departmentService = departmentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PositionDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;
        PositionDTO positionDTO = (PositionDTO) target;
        if (positionDTO.getId() != null) return;
        checkUniqueName(positionDTO, errors);
        checkValidLeading(positionDTO, errors);
    }

    private void checkUniqueName(PositionDTO positionDTO, Errors errors) {
        if (!positionService.existsByPositionName(positionDTO.getPositionName())) return;

        String departmentName = departmentService.getByPositionName(positionDTO.getPositionName()).getDepartmentName();
        errors.rejectValue("positionName", "",
                String.format("Должность с таким названием существует в отделе: %s", departmentName));
    }

    private void checkValidLeading(PositionDTO positionDTO, Errors errors) {
        if (!positionDTO.isLeading()) return;

        if (positionService.existsByLeadingAndDepartmentId(true, positionDTO.getDepartmentId()))
            errors.rejectValue("leading", "", "Управляющая должность для выбранного отдела уже существует!");
    }
}
