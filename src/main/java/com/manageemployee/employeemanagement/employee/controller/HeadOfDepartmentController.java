package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.dto.SearchEmployeeFilters;
import com.manageemployee.employeemanagement.employee.dto.mapper.EmployeeMapper;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.position.dto.mapper.PositionMapper;
import com.manageemployee.employeemanagement.position.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/myPage/headOfDepartment")
public class HeadOfDepartmentController {
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final EmployeeMapper employeeMapper;
    private final PositionMapper positionMapper;

    @Autowired
    public HeadOfDepartmentController(EmployeeService employeeService,
                                      PositionService positionService,
                                      EmployeeMapper employeeMapper,
                                      PositionMapper positionMapper) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.employeeMapper = employeeMapper;
        this.positionMapper = positionMapper;
    }

    @GetMapping("/myEmployees")
    public String getMyEmployees(SearchEmployeeFilters filters,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Employee employee = loadFromUser(userDetails);
        Long companyBranchId = employee.getCompanyBranch().getId();
        Long departmentId = employee.getPosition().getDepartment().getId();
        if (filters == null)
            filters = new SearchEmployeeFilters(companyBranchId, departmentId);

        List<EmployeeDTO> employeeDTOS = employeeMapper.toDtoList(employeeService.getAllEmployee(filters));
        List<PositionDTO> positionDTOS =
                positionMapper.toDtoList(positionService.getPositionsByDepartmentId(departmentId));

        model.addAttribute("employeeDTOS", employeeDTOS);
        model.addAttribute("positions", positionDTOS);
        model.addAttribute("statuses", List.of(EmployeeStatus.values()));
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("departmentId", departmentId);

        return "employee/headOfDepartment/subordinateEmployees";
    }

    private Employee loadFromUser(UserDetails userDetails) {
        return employeeService.getByEmail(userDetails.getUsername()).orElseThrow(() ->
                new IllegalArgumentException("Загружен не существующий работник!"));
    }
}
