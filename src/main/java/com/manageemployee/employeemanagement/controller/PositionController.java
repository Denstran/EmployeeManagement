package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/positions")
public class PositionController {
    private final PositionService positionService;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionController(PositionService positionService, PositionMapper positionMapper) {
        this.positionService = positionService;
        this.positionMapper = positionMapper;
    }
}
