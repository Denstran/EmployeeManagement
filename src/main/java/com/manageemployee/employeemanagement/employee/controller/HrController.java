package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentMapper;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.dto.SearchEmployeeFilters;
import com.manageemployee.employeemanagement.employee.dto.mapper.EmployeeMapper;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.validation.employee.EmployeeValidator;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.position.dto.mapper.PositionMapper;
import com.manageemployee.employeemanagement.position.service.PositionService;
import com.manageemployee.employeemanagement.util.validationgroups.DefaultGroup;
import com.manageemployee.employeemanagement.util.validationgroups.UpdatingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/myPage/hr")
public class HrController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final EmployeeMapper employeeMapper;
    private final DepartmentMapper departmentMapper;
    private final PositionMapper positionMapper;
    private final EmployeeValidator employeeValidator;

    @Autowired
    public HrController(EmployeeService employeeService, DepartmentService departmentService,
                        PositionService positionService,
                        EmployeeMapper employeeMapper, DepartmentMapper departmentMapper, PositionMapper positionMapper, EmployeeValidator employeeValidator) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.employeeMapper = employeeMapper;
        this.departmentMapper = departmentMapper;
        this.positionMapper = positionMapper;
        this.employeeValidator = employeeValidator;
    }

    @GetMapping("/employees")
    public String getCompanyBranchEmployee(SearchEmployeeFilters filters,
                                           @AuthenticationPrincipal UserDetails userDetails,
                                           Model model) {
        Employee hr = loadFromUser(userDetails);
        Long companyBranchId = hr.getCompanyBranch().getId();
        if (filters == null)
            filters = new SearchEmployeeFilters(companyBranchId);

        List<EmployeeDTO> employeeDTOS = employeeMapper.toDtoList(employeeService.getAllEmployee(filters));
        List<DepartmentDTO> departmentDTOS = departmentMapper.toDtoList(departmentService.getAllDepartments());
        List<PositionDTO> positionDTOS = positionMapper.toDtoList(positionService.getAllPositions());
        List<EmployeeStatus> employeeStatuses = List.of(EmployeeStatus.values());

        model.addAttribute("departments", departmentDTOS);
        model.addAttribute("positions", positionDTOS);
        model.addAttribute("statuses", employeeStatuses);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("employeeDTOS", employeeDTOS);

        return "employee/hr/allEmployee";
    }

    @GetMapping("/employees/{employeeId}/update")
    public String updateEmployeeForm(Model model, @PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeMapper.toDto(employeeService.getById(employeeId));
        List<PositionDTO> positionDTOS = positionMapper.toDtoList(positionService.getAllPositions());
        List<EmployeeStatus> employeeStatuses = List.of(EmployeeStatus.values());

        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("positionDTOS", positionDTOS);
        model.addAttribute("employeeStatuses", employeeStatuses);

        return "employee/hr/updateEmployee";
    }

    @PostMapping("/employees/{employeeId}/update")
    public String updateEmployee(@ModelAttribute("employeeDTO")
                                     @Validated({DefaultGroup.class, UpdatingGroup.class}) EmployeeDTO employeeDTO,
                                     BindingResult bindingResult,
                                     @PathVariable("employeeId") Long employeeId,
                                     Model model) {
        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = positionMapper.toDtoList(positionService.getAllPositions());
            List<EmployeeStatus> employeeStatuses = List.of(EmployeeStatus.values());
            model.addAttribute("positionDTOS", positionDTOS);
            model.addAttribute("employeeStatuses", List.of(EmployeeStatus.values()));

            return "employee/hr/updateEmployee";
        }

        employeeService.updateEmployee(employeeMapper.toEntity(employeeDTO));
        return "redirect:/myPage/hr/employees";
    }

    @GetMapping("/employees/hireEmployee")
    public String hireEmployeeForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Employee employee = loadFromUser(userDetails);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setCompanyBranchId(employee.getCompanyBranch().getId());

        List<PositionDTO> positionDTOS = positionMapper.toDtoList(positionService.getAllPositions());

        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("positionDTOS", positionDTOS);

        return "employee/hr/createEmployee";
    }

    @PostMapping("/employees/hireEmployee")
    public String hireEmployee(@ModelAttribute("employeeDTO") @Validated(DefaultGroup.class) EmployeeDTO employeeDTO,
                               BindingResult bindingResult,
                               Model model) {
        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = positionMapper.toDtoList(positionService.getAllPositions());
            model.addAttribute("positionDTOS", positionDTOS);
            return "employee/hr/createEmployee";
        }

        employeeService.createEmployee(employeeMapper.toEntity(employeeDTO));
        return "redirect:/myPage/hr/employees";
    }

    private Employee loadFromUser(UserDetails userDetails) {
        return employeeService.getByEmail(userDetails.getUsername()).orElseThrow(() ->
            new IllegalArgumentException("Загружен не существующий работник!"));
    }
}
