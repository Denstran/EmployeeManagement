package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.dto.EmployeePaymentLogDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.EmployeePaymentLogMapper;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/myPage")
@Slf4j
public class EmployeePageController {
    private final EmployeeControllerFacade controllerFacade;
    private final EmployeePaymentLogService employeePaymentLogService;
    private final EmployeePaymentLogMapper mapper;

    private static final String EMPLOYEE_PRIVATE_PAGE = "employee/employeePrivatePage";

    @Autowired
    public EmployeePageController(EmployeeControllerFacade controllerFacade,
                                  EmployeePaymentLogService employeePaymentLogService,
                                  EmployeePaymentLogMapper mapper) {
        this.controllerFacade = controllerFacade;
        this.employeePaymentLogService = employeePaymentLogService;
        this.mapper = mapper;
    }

    @GetMapping
    public String getEmployeePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("FROM EMPLOYEE_CONTROLLER: RECEIVED REQUEST FOR PRIVATE PAGE FROM USER: {} ROLES {}",
                userDetails, userDetails.getAuthorities());
        CompanyBranchDTO companyBranchDTO = controllerFacade.getCompanyBranchDTO(userDetails);
        EmployeeDTO employeeDTO = controllerFacade.getEmployeeDTOFromUser(userDetails);

        model.addAttribute("companyBranchDTO", companyBranchDTO);
        model.addAttribute("employeeDTO", employeeDTO);
        return EMPLOYEE_PRIVATE_PAGE;
    }

    @GetMapping("/myPayments")
    public String getMyPayments(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(name = "startDate", required = false) String startDate,
                                @RequestParam(name = "endDate", required = false) String endDate,
                                @RequestParam(name = "transferAction", required = false) String transferAction) {
        EmployeeDTO employeeDTO = controllerFacade.getEmployeeDTOFromUser(userDetails);
        List<EmployeePaymentLogDTO> paymentLogDTOS = mapper.toDtoList(
                employeePaymentLogService.getEmployeePaymentLog(
                        employeeDTO.getId(), startDate, endDate, transferAction));

        model.addAttribute("paymentLogDTOS", paymentLogDTOS);
        return "payments/myPayments";
    }
}
