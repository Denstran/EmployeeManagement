package com.manageemployee.employeemanagement.department.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentInfoMapper;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentMapper;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.department.validation.departmentInfoValidation.DepartmentInfoValidator;
import com.manageemployee.employeemanagement.util.validationgroups.DefaultGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches")
public class DepartmentInfoController {
    private final DepartmentInfoMapper departmentInfoMapper;
    private final DepartmentInfoValidator departmentInfoValidator;
    private final DepartmentInfoService departmentInfoService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    private final String ADD_DEPARTMENT_FORM = "companyBranch/addDepartmentForm";
    private final String SHOW_COMPANY_BRANCH_DEPARTMENTS = "companyBranch/companyBranchDepartments";
    private final String REDIRECT_URL_DEPARTMENTS = "redirect:/companyBranches/%d/departments";

    public DepartmentInfoController(DepartmentInfoMapper departmentInfoMapper,
                                    DepartmentInfoValidator departmentInfoValidator,
                                    DepartmentInfoService departmentInfoService,
                                    DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentInfoMapper = departmentInfoMapper;
        this.departmentInfoValidator = departmentInfoValidator;
        this.departmentInfoService = departmentInfoService;
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping("/{companyBranchId}/departments")
    public String getDepartments(@PathVariable("companyBranchId") Long companyBranchId, Model model) {
        List<DepartmentInfoDTO> departmentInfoDTOS =
                departmentInfoMapper.toDtoList(departmentInfoService.getDepartmentsByCompanyBranchId(companyBranchId));
        model.addAttribute("departmentInfoDTOS", departmentInfoDTOS);
        model.addAttribute("companyBranchId", companyBranchId);

        return SHOW_COMPANY_BRANCH_DEPARTMENTS;
    }

    @GetMapping("/{companyBranchId}/departments/add")
    public String addDepartmentInfoForm(@PathVariable("companyBranchId") Long companyBranchId, Model model,
                                        HttpSession session) {
        DepartmentInfoDTO dto = new DepartmentInfoDTO();
        List<DepartmentDTO> availableDepartments =
                departmentMapper.toDtoList(departmentService.getAvailableDepartments(companyBranchId));

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
        departmentInfoValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<DepartmentDTO> availableDepartments =
                    (List<DepartmentDTO>) session.getAttribute("availableDepartments");
            model.addAttribute("availableDepartments", availableDepartments);
            model.addAttribute("companyBranchId", companyBranchId);
            model.addAttribute("isUpdating", false);
            return ADD_DEPARTMENT_FORM;
        }

        DepartmentInfo departmentInfo = departmentInfoMapper.toEntity(dto);
        departmentInfoService.create(departmentInfo);

        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }

    @GetMapping("/{companyBranchId}/departments/update")
    public String updateDepartmentInfoForm(@PathVariable Long companyBranchId, @RequestParam("depId") Long depId,
                                           Model model, HttpSession session) {
        DepartmentInfo departmentInfo = departmentInfoService.getById(companyBranchId, depId);
        DepartmentInfoDTO dto = departmentInfoMapper.toDto(departmentInfo);
        DepartmentDTO department = departmentMapper.toDto(departmentInfo.getPk().getDepartment());

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
        departmentInfoValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            DepartmentDTO department =
                    (DepartmentDTO) session.getAttribute("departmentSelected");
            model.addAttribute("departmentSelected", department);
            model.addAttribute("companyBranchId", companyBranchId);
            model.addAttribute("isUpdating", true);

            return ADD_DEPARTMENT_FORM;
        }

        departmentInfoService.update(departmentInfoMapper.toEntity(dto));

        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }

    @PostMapping("/{companyBranchId}/departments/{departmentId}/remove")
    public String removeDepartmentInfo(@PathVariable Long companyBranchId, @PathVariable Long departmentId) {
        DepartmentInfo departmentInfo = departmentInfoService.getById(companyBranchId, departmentId);
        departmentInfoService.deleteDepartmentInfo(departmentInfo);
        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }
}
