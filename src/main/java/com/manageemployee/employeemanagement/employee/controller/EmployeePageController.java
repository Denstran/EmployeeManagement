package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/myPage")
@Slf4j
public class EmployeePageController {
    private final EmployeeControllerFacade controllerFacade;

    private static final String EMPLOYEE_PRIVATE_PAGE = "employee/employeePrivatePage";

    @Autowired
    public EmployeePageController(EmployeeControllerFacade controllerFacade) {
        this.controllerFacade = controllerFacade;
    }

    @GetMapping(path = "/myPage")
    public String getEmployeePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("FROM EMPLOYEE_CONTROLLER: RECEIVED REQUEST FOR PRIVATE PAGE FROM USER: {} ROLES {}",
                userDetails, userDetails.getAuthorities());
        CompanyBranchDTO companyBranchDTO = controllerFacade.getCompanyBranchDTO(userDetails);
        EmployeeDTO employeeDTO = controllerFacade.getEmployeeDTOFromUser(userDetails);

        model.addAttribute("companyBranchDTO", companyBranchDTO);
        model.addAttribute("employeeDTO", employeeDTO);
        return EMPLOYEE_PRIVATE_PAGE;
    }
}
