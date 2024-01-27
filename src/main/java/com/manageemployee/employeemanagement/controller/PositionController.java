package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.validators.PositionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/positions")
public class PositionController {
    private final PositionService positionService;
    private final PositionValidator positionValidator;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionController(PositionService positionService, PositionValidator positionValidator,
                              PositionMapper positionMapper) {
        this.positionService = positionService;
        this.positionValidator = positionValidator;
        this.positionMapper = positionMapper;
    }
}
