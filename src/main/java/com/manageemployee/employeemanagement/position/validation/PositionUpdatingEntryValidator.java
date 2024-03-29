package com.manageemployee.employeemanagement.position.validation;

import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.position.service.PositionService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@ValidatorQualifier(validatorKey = "positionSubValidator")
public class PositionUpdatingEntryValidator implements Validator {
    private final PositionService positionService;
    private final DepartmentService departmentService;

    @Autowired
    public PositionUpdatingEntryValidator(PositionService positionService, DepartmentService departmentService) {
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
        if (positionDTO.getId() == null) return;

        checkUniqueName(positionDTO, errors);
        checkValidLeading(positionDTO, errors);
    }

    private void checkValidLeading(PositionDTO positionDTO, Errors errors) {
        if (!positionDTO.isLeading()) return;

        Optional<Position> positionOptional =
                positionService.getByLeadingAndDepartmentId(true, positionDTO.getDepartmentId());
        if (positionOptional.isEmpty()) return;

        Position position = positionOptional.get();
        if (position.getId().equals(positionDTO.getId())) return;

        errors.rejectValue("isLeading", "",
                "Управляющая должность для выбранного отдела уже существует!");
    }

    private void checkUniqueName(PositionDTO positionDTO, Errors errors) {
        Optional<Position> positionOptional = positionService.getByName(positionDTO.getPositionName());
        if (positionOptional.isEmpty()) return;

        Position position = positionOptional.get();
        if (position.getId().equals(positionDTO.getId())) return;

        String departmentName = departmentService.getByPositionName(positionDTO.getPositionName()).getDepartmentName();
        errors.rejectValue("positionName", "",
                String.format("Должность с таким названием существует в отделе: %s", departmentName));
    }
}
