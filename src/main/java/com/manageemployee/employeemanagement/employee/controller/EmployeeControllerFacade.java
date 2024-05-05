package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.companyBranch.dto.mappers.CompanyBranchMapper;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.dto.SearchEmployeeFilters;
import com.manageemployee.employeemanagement.employee.dto.mapper.EmployeeMapper;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.validation.EmployeeValidator;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.position.dto.mapper.PositionMapper;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.position.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
public class EmployeeControllerFacade {
    private final EmployeeService employeeService;
    private final CompanyBranchMapper companyBranchMapper;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final PositionMapper positionMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;

    @Autowired
    public EmployeeControllerFacade(EmployeeService employeeService,
                                    CompanyBranchMapper companyBranchMapper,
                                    DepartmentService departmentService,
                                    PositionService positionService,
                                    PositionMapper positionMapper,
                                    EmployeeMapper employeeMapper, EmployeeValidator employeeValidator) {
        this.employeeService = employeeService;
        this.companyBranchMapper = companyBranchMapper;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.positionMapper = positionMapper;
        this.employeeMapper = employeeMapper;
        this.employeeValidator = employeeValidator;
    }

    public List<PositionDTO> getPositionDTOList(String departmentId) {
        return positionMapper.toDtoList(getPositions(Long.valueOf(departmentId)));
    }

    private List<Position> getPositions(Long departmentId) {
        return positionService.getPositionsByDepartmentId(departmentId);
    }

    public EmployeeDTO getEmployeeDTO(String employeeId) {
        return employeeMapper.toDto(employeeService.getById(Long.valueOf(employeeId)));
    }

    public List<EmployeeDTO> getEmployeeDTOListFiltered(SearchEmployeeFilters filters) {
        return employeeMapper.toDtoList(employeeService.getAllEmployee(filters));
    }

    public String getDepartmentName(Long departmentId) {
        return departmentService.getById(departmentId).getDepartmentName();
    }

    public void validate(EmployeeDTO dto, BindingResult bindingResult) {
        employeeValidator.validate(dto, bindingResult);
    }

    public void createEmployee(EmployeeDTO dto) {
        employeeService.createEmployee(employeeMapper.toEntity(dto));
    }

    public void updateEmployee(EmployeeDTO dto) {
        employeeService.updateEmployee(employeeMapper.toEntity(dto));
    }

    public CompanyBranchDTO getCompanyBranchDTO(UserDetails user) {
        return companyBranchMapper.toDto(getEmployeeByUser(user).getCompanyBranch());
    }

    public EmployeeDTO getEmployeeDTOFromUser(UserDetails user) {
        return employeeMapper.toDto(getEmployeeByUser(user));
    }

    private Employee getEmployeeByUser(UserDetails user) {
        return employeeService.getByEmail(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No user with such email was found!"));
    }
}
