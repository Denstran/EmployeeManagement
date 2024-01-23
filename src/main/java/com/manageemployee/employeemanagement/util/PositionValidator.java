package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class PositionValidator implements Validator {

    private final PositionService positionService;

    @Autowired
    public PositionValidator(PositionService positionService) {
        this.positionService = positionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PositionDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PositionDTO positionDTO = (PositionDTO) target;

    }

}
