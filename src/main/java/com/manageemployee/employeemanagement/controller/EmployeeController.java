package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeStatusMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.EmployeeStatusService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeStatusService employeeStatusService;
    private final EmployeeStatusMapper employeeStatusMapper;
    private final MoneyService moneyService;
    private final PositionService positionService;
    private final PositionMapper positionMapper;

    private final String REDIRECT_PATH = "redirect:/companyBranches/%d/departments/%d/employees";
    private final String SHOW_EMPLOYEE = "employee/employee";
    private final String VIEW_FOR_UPDATE_OR_CREATE = "employee/createOrUpdateEmployee";

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper,
                              EmployeeStatusService employeeStatusService,
                              EmployeeStatusMapper employeeStatusMapper, MoneyService moneyService,
                              PositionService positionService, PositionMapper positionMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.employeeStatusService = employeeStatusService;
        this.employeeStatusMapper = employeeStatusMapper;
        this.moneyService = moneyService;
        this.positionService = positionService;
        this.positionMapper = positionMapper;
    }


}
