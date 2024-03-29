package com.manageemployee.employeemanagement.department.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentMapper;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.department.validation.departmentValidation.DepartmentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidator departmentValidator;

    // Patterns for return values
    private final String VIEW_FOR_UPDATE_OR_CREATE = "department/createOrUpdateDepartment";
    private final String SHOW_VIEW = "department/departments";
    private final String REDIRECT_LINK = "redirect:/departments";


    @Autowired
    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper,
                                DepartmentValidator departmentValidator) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
        this.departmentValidator = departmentValidator;
    }

    @GetMapping
    public String getDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentDTO> departmentDTOS = departmentMapper.toDtoList(departments);
        model.addAttribute("departmentDTOS",departmentDTOS);
        model.addAttribute("departmentDTO", new DepartmentDTO());

        return SHOW_VIEW;
    }

    @GetMapping("/new")
    public String createDepartmentForm(Model model) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        model.addAttribute("departmentDTO", departmentDTO);
        model.addAttribute("isUpdating", false);
        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/new")
    public String createDepartment(@ModelAttribute("departmentDTO") @Valid DepartmentDTO departmentDTO,
                                   BindingResult bindingResult) {
        departmentValidator.validate(departmentDTO, bindingResult);
        if (bindingResult.hasErrors()) return VIEW_FOR_UPDATE_OR_CREATE;
        departmentService.createDepartment(departmentMapper.toEntity(departmentDTO));

        return REDIRECT_LINK;
    }

    @GetMapping("/{depId}/update")
    public String updateDepartmentForm(@PathVariable("depId") Long depId, Model model) {
        Department department = departmentService.getById(depId);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        model.addAttribute("departmentDTO", departmentDTO);
        model.addAttribute("isUpdating", true);
        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/{depId}/update")
    public String updateDepartment(@ModelAttribute("departmentDTO") @Valid DepartmentDTO departmentDTO,
                                   BindingResult bindingResult, @PathVariable("depId") Long depId, Model model) {
        departmentValidator.validate(departmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("isUpdating", true);
            return VIEW_FOR_UPDATE_OR_CREATE;
        }

        departmentService.createDepartment(departmentMapper.toEntity(departmentDTO));
        return REDIRECT_LINK;
    }
}
