package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.DepartmentMapper;
import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.util.DepartmentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final CompanyBranchService companyBranchService;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidator departmentValidator;

    // Patterns for return values
    private final String VIEW_FOR_UPDATE_OR_CREATE = "department/createOrUpdateDepartment";
    private final String SHOW_VIEW = "department/departments";
    private final String REDIRECT_PATTERN = "redirect:/companyBranches/%d/departments";


    @Autowired
    public DepartmentController(DepartmentService departmentService, CompanyBranchService companyBranchService,
                                DepartmentMapper departmentMapper, DepartmentValidator departmentValidator) {
        this.departmentService = departmentService;
        this.companyBranchService = companyBranchService;
        this.departmentMapper = departmentMapper;
        this.departmentValidator = departmentValidator;
    }

    @GetMapping()
    public String getAllDepartmentsOfBranch(@PathVariable("companyBranchId") Long companyBranchId,
                                            Model model) {
        List<Department> departmentList = departmentService.getAllDepartmentsByCompanyBranchId(companyBranchId);
        List<DepartmentDTO> departmentDTOS = departmentMapper.toDtoList(departmentList);

        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("departments", departmentDTOS);
        return SHOW_VIEW;
    }

    @GetMapping("/new")
    public String createDepartmentForm(@PathVariable("companyBranchId") Long companyBranchId, Model model) {
        DepartmentDTO departmentDTO = new DepartmentDTO();

        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("departmentDTO", departmentDTO);
        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/new")
    public String createDepartment(@ModelAttribute("departmentDTO") @Valid DepartmentDTO departmentDTO,
                                   BindingResult bindingResult, @PathVariable("companyBranchId") Long companyBranchId) {
        departmentValidator.validate(departmentDTO, bindingResult);
        if (bindingResult.hasErrors()) return VIEW_FOR_UPDATE_OR_CREATE;

        Department department = departmentMapper.toEntity(departmentDTO);
        CompanyBranch companyBranch = companyBranchService.getCompanyBranchReferenceById(companyBranchId);
        department.setCompanyBranch(companyBranch);
        departmentService.saveDepartment(department);

        return String.format(REDIRECT_PATTERN, companyBranchId);
    }

    @GetMapping("/{depId}/update")
    public String updateDepartmentForm(@PathVariable("companyBranchId") Long companyBranchId,
                                       @PathVariable("depId") Long depId,
                                       Model model) {
        Department department = departmentService.getDepartmentById(depId);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        boolean isUpdating = true;
        model.addAttribute("departmentDTO", departmentDTO);
        model.addAttribute("isUpdating", isUpdating);
        model.addAttribute("companyBranchId", companyBranchId);

        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/{depId}/update")
    public String updateDepartment(@ModelAttribute("departmentDTO") @Valid DepartmentDTO departmentDTO,
                                   BindingResult bindingResult, @RequestParam("isUpdating") boolean isUpdating,
                                   @PathVariable("companyBranchId") Long companyBranchId,
                                   Model model) {
        departmentValidator.validate(departmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("isUpdating", isUpdating);
            return VIEW_FOR_UPDATE_OR_CREATE;
        }

        Department department = departmentMapper.toEntity(departmentDTO);
        departmentService.updateDepartment(department);

        return String.format(REDIRECT_PATTERN, companyBranchId);
    }

    @PostMapping("/{depId}/delete")
    public String deleteDepartment(@PathVariable("companyBranchId") Long companyBranchId,
                                   @PathVariable("depId") Long depId) {
        Department department = departmentService.getDepartmentById(depId);

        departmentService.deleteDepartment(department);
        return String.format(REDIRECT_PATTERN, companyBranchId);
    }
}
