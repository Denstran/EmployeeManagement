package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.enumTypes.EmployeeStatus;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.validators.employeeValidators.EmployeeValidator;
import com.manageemployee.employeemanagement.validationgroups.DefaultGroup;
import com.manageemployee.employeemanagement.validationgroups.UpdatingGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final PositionMapper positionMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;
    private final String SHOW_EMPLOYEE = "employee/employee";
    private final String CREATE_OR_UPDATE_FORM = "employee/createOrUpdateEmployee";
    private final String REDIRECT_LINK = "redirect:/companyBranches/%d/departments/%d/employees";

    @Autowired
    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService,
                              PositionService positionService, PositionMapper positionMapper,
                              EmployeeMapper employeeMapper, EmployeeValidator validator) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.positionMapper = positionMapper;
        this.employeeMapper = employeeMapper;
        this.employeeValidator = validator;
    }

    @GetMapping
    public String getEmployees(Model model, @PathVariable Long companyBranchId, @PathVariable Long depId) {
        List<EmployeeDTO> employeeDTOS =
                employeeMapper.toDtoList(employeeService.getEmployeesByCompanyBranchAndDepartment(companyBranchId, depId));
        String departmentName = departmentService.getById(depId).getDepartmentName();
        model.addAttribute("employeeDTOS", employeeDTOS);
        model.addAttribute("departmentName", departmentName);

        return SHOW_EMPLOYEE;
    }

    @GetMapping("/new")
    public String createEmployeeForm(Model model, @PathVariable Long companyBranchId, @PathVariable Long depId,
                                     HttpSession session) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        setupModel(model, depId, false, employeeDTO, session);

        return CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("employeeDTO") @Validated(DefaultGroup.class) EmployeeDTO employeeDTO,
                                 BindingResult bindingResult, Model model, HttpSession session,
                                 @PathVariable Long depId, @PathVariable Long companyBranchId) {
        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = (List<PositionDTO>) session.getAttribute("positionDTOS");
            model.addAttribute("positionDTOS", positionDTOS);
            return CREATE_OR_UPDATE_FORM;
        }

        employeeDTO.setCompanyBranchId(companyBranchId);

        employeeService.createEmployee(employeeMapper.toEntity(employeeDTO));
        return String.format(REDIRECT_LINK, companyBranchId, depId);
    }

    @GetMapping("/{employeeId}/update")
    public String updateEmployeeFrom(@PathVariable Long companyBranchId,
                                     @PathVariable Long depId,
                                     @PathVariable Long employeeId, Model model, HttpSession session) {
        setupModelForUpdating(model, depId, employeeId, session);

        return CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/{employeeId}/update")
    public String updateEmployee(@ModelAttribute("employeeDTO")
                                     @Validated({DefaultGroup.class, UpdatingGroup.class}) EmployeeDTO employeeDTO,
                                     BindingResult bindingResult, Model model, HttpSession session,
                                     @PathVariable Long depId,
                                     @PathVariable Long employeeId,
                                     @PathVariable Long companyBranchId) {
        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = (List<PositionDTO>) session.getAttribute("positionDTOS");
            List<EmployeeStatus> employeeStatuses = List.of(EmployeeStatus.values());
            model.addAttribute("positionDTOS", positionDTOS);
            model.addAttribute("employeeStatuses", employeeStatuses);
            model.addAttribute("isUpdating", true);
            return CREATE_OR_UPDATE_FORM;
        }

        employeeService.updateEmployee(employeeMapper.toEntity(employeeDTO));
        return String.format(REDIRECT_LINK, companyBranchId, depId);
    }

    @PostMapping("/{employeeId}/delete")
    public String deleteEmployee(@PathVariable Long companyBranchId, @PathVariable Long depId,
                                 @PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeService.getById(employeeId));
        return String.format(REDIRECT_LINK, companyBranchId, depId);
    }

    private void setupModel(Model model, Long departmentId, boolean isUpdating, EmployeeDTO employeeDTO, HttpSession session) {
        List<PositionDTO> positionDTOS = positionMapper.toDtoList(positionService.getPositionsByDepartmentId(departmentId));
        model.addAttribute("positionDTOS", positionDTOS);
        model.addAttribute("isUpdating", isUpdating);
        model.addAttribute("employeeDTO", employeeDTO);

        session.setAttribute("positionDTOS", positionDTOS);
    }

    private void setupModelForUpdating(Model model, Long departmentId, Long employeeId, HttpSession session) {
        EmployeeDTO employeeDTO = employeeMapper.toDto(employeeService.getById(employeeId));
        List<EmployeeStatus> employeeStatuses = List.of(EmployeeStatus.values());
        model.addAttribute("employeeStatuses", employeeStatuses);
        setupModel(model, departmentId, true, employeeDTO, session);
    }

}