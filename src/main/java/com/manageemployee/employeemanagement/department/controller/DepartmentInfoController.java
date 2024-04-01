package com.manageemployee.employeemanagement.department.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.util.validationgroups.DefaultGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches")
public class DepartmentInfoController {
    private final DepartmentInfoControllerFacade controllerFacade;
    private final String ADD_DEPARTMENT_FORM = "companyBranch/addDepartmentForm";
    private final String SHOW_COMPANY_BRANCH_DEPARTMENTS = "companyBranch/companyBranchDepartments";
    private final String REDIRECT_URL_DEPARTMENTS = "redirect:/companyBranches/%d/departments";

    @Autowired
    public DepartmentInfoController(DepartmentInfoControllerFacade controllerFacade) {
        this.controllerFacade = controllerFacade;
    }


    @GetMapping("/{companyBranchId}/departments")
    public String getDepartments(@PathVariable("companyBranchId") Long companyBranchId, Model model) {
        List<DepartmentInfoDTO> departmentInfoDTOS = controllerFacade.getDepartmentInfoDTOS(companyBranchId);
        model.addAttribute("departmentInfoDTOS", departmentInfoDTOS);
        model.addAttribute("companyBranchId", companyBranchId);

        return SHOW_COMPANY_BRANCH_DEPARTMENTS;
    }

    @GetMapping("/{companyBranchId}/departments/add")
    public String addDepartmentInfoForm(@PathVariable("companyBranchId") Long companyBranchId, Model model,
                                        HttpSession session) {
        DepartmentInfoDTO dto = new DepartmentInfoDTO();
        List<DepartmentDTO> availableDepartments = controllerFacade.getAvailableDepartmentsDTO(companyBranchId);

        model.addAttribute("dto", dto);
        model.addAttribute("availableDepartments", availableDepartments);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("isUpdating", false);
        session.setAttribute("availableDepartments", availableDepartments);

        return ADD_DEPARTMENT_FORM;
    }

    @PostMapping("/{companyBranchId}/departments/add")
    public String addDepartmentInfo(@ModelAttribute("dto") @Validated(DefaultGroup.class) DepartmentInfoDTO dto,
                                    BindingResult bindingResult,
                                    Model model, @PathVariable("companyBranchId") Long companyBranchId,
                                    HttpSession session) {
        dto.setCompanyBranchId(companyBranchId);
        controllerFacade.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<DepartmentDTO> availableDepartments =
                    (List<DepartmentDTO>) session.getAttribute("availableDepartments");
            model.addAttribute("availableDepartments", availableDepartments);
            model.addAttribute("companyBranchId", companyBranchId);
            model.addAttribute("isUpdating", false);
            return ADD_DEPARTMENT_FORM;
        }
        controllerFacade.createDepartmentInfo(dto);
        session.removeAttribute("availableDepartments");
        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }

    @GetMapping("/{companyBranchId}/departments/update")
    public String updateDepartmentInfoForm(@PathVariable Long companyBranchId, @RequestParam("depId") Long depId,
                                           Model model, HttpSession session) {
        DepartmentInfoDTO dto = controllerFacade.getDepartmentInfoDTO(companyBranchId, depId);
        DepartmentDTO department = controllerFacade.getDepartmentDTOFromDepartmentInfo(companyBranchId, depId);

        model.addAttribute("dto", dto);
        model.addAttribute("departmentSelected", department);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("isUpdating", true);

        session.setAttribute("departmentSelected", department);

        return ADD_DEPARTMENT_FORM;
    }

    @PostMapping("/{companyBranchId}/departments/update")
    public String updateDepartmentInfo(@ModelAttribute("dto") @Validated(DefaultGroup.class) DepartmentInfoDTO dto,
                                       BindingResult bindingResult, @PathVariable Long companyBranchId,
                                       Model model, HttpSession session) {
        controllerFacade.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            DepartmentDTO department =
                    (DepartmentDTO) session.getAttribute("departmentSelected");
            model.addAttribute("departmentSelected", department);
            model.addAttribute("companyBranchId", companyBranchId);
            model.addAttribute("isUpdating", true);

            return ADD_DEPARTMENT_FORM;
        }
        controllerFacade.updateDepartmentInfo(dto);

        session.removeAttribute("departmentSelected");
        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }

    @PostMapping("/{companyBranchId}/departments/{departmentId}/remove")
    public String removeDepartmentInfo(@PathVariable Long companyBranchId, @PathVariable Long departmentId) {
        controllerFacade.removeDepartment(companyBranchId, departmentId);
        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }
}
