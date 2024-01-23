package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.PositionValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
