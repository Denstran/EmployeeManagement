package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeStatusMapper;
import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.dto.EmployeeStatusDTO;
import com.manageemployee.employeemanagement.model.*;
import com.manageemployee.employeemanagement.service.*;
import com.manageemployee.employeemanagement.util.EmployeeValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;
    private final DepartmentService departmentService;
    private final EmployeeStatusService employeeStatusService;
    private final CompanyBranchService companyBranchService;
    private final EmployeeStatusMapper employeeStatusMapper;
    private final MoneyService moneyService;

    private final String REDIRECT_PATH = "redirect:/companyBranches/%d/departments/%d/employees";
    private final String SHOW_EMPLOYEE = "employee/employee";
    private final String VIEW_FOR_UPDATE_OR_CREATE = "employee/createOrUpdateEmployee";

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper,
                              EmployeeValidator employeeValidator, DepartmentService departmentService,
                              EmployeeStatusService employeeStatusService, CompanyBranchService companyBranchService,
                              EmployeeStatusMapper employeeStatusMapper, MoneyService moneyService) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.employeeValidator = employeeValidator;
        this.departmentService = departmentService;
        this.employeeStatusService = employeeStatusService;
        this.companyBranchService = companyBranchService;
        this.employeeStatusMapper = employeeStatusMapper;
        this.moneyService = moneyService;
    }

    @GetMapping()
    public String getAllEmployee(@PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId, Model model) {
        List<Employee> employees = employeeService.getAllEmployeesInDepartment(depId);
        List<EmployeeDTO> employeeDTOS = employeeMapper.toDtoList(employees);

        model.addAttribute("employees", employeeDTOS);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);
        return SHOW_EMPLOYEE;
    }

    @GetMapping("/new")
    public String createEmployeeForm(@PathVariable("companyBranchId") Long companyBranchId,
                                     @PathVariable("depId") Long depId, Model model) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDepartmentId(depId);

        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);

        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("employeeDTO") @Valid EmployeeDTO employeeDTO,
                                 BindingResult bindingResult,
                                 @PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId) {
        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors())
            return VIEW_FOR_UPDATE_OR_CREATE;

        Employee employee = employeeMapper.toEntity(employeeDTO);
        Department department = departmentService.getDepartmentReferenceById(depId);
        EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatusReferenceById(3L);

        employee.setDepartment(department);
        employee.setEmployeeStatus(employeeStatus);
        employeeService.saveEmployee(employee);
        moneyService.handleEmployeeSalaryChanges(employeeDTO, companyBranchId);

        return String.format(REDIRECT_PATH, companyBranchId, depId);
    }

    @GetMapping("/{empId}/update")
    public String updateEmployeeForm(@PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("empId") Long empId, Model model) {
        Employee employee = employeeService.getEmployeeById(empId);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        List<EmployeeStatus> employeeStatuses = employeeStatusService.getEmployeeStatuses();
        List<EmployeeStatusDTO> employeeStatusDTOS = employeeStatusMapper.toDtoList(employeeStatuses);

        model.addAttribute("employeeStatusDTOS", employeeStatusDTOS);
        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);
        model.addAttribute("isUpdating", true);

        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/{empId}/update")
    public String updateEmployee(@ModelAttribute("employeeDTO") @Valid EmployeeDTO employeeDTO,
                                 BindingResult bindingResult,
                                 @PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("empId") Long empId, Model model) {
        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<EmployeeStatus> employeeStatuses = employeeStatusService.getEmployeeStatuses();
            List<EmployeeStatusDTO> employeeStatusDTOS = employeeStatusMapper.toDtoList(employeeStatuses);

            model.addAttribute("employeeStatusDTOS", employeeStatusDTOS);
            model.addAttribute("isUpdating", true);
            return VIEW_FOR_UPDATE_OR_CREATE;
        }
        moneyService.handleEmployeeSalaryChanges(employeeDTO, companyBranchId);

        Employee employee = employeeMapper.toEntity(employeeDTO);
        employeeService.updateEmployee(employee);

        return String.format(REDIRECT_PATH, companyBranchId, depId);
    }

    @PostMapping("/{empId}/delete")
    public String deleteEmployee(@PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("empId") Long empId) {
        Employee employee = employeeService.getEmployeeById(empId);
        CompanyBranch companyBranch = companyBranchService.getCompanyBranchById(companyBranchId);
        companyBranch.getBudget().setAmount(companyBranch.getBudget().getAmount()
                .add(employee.getSalary().getAmount()));

        companyBranchService.updateCompanyBranch(companyBranch);
        employeeService.deleteEmployeeById(empId);
        return String.format(REDIRECT_PATH, companyBranchId, depId);
    }
}
