package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.EmployeeMapper;
import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.validation.EmployeeValidator;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.position.dto.mapper.PositionMapper;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.position.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
public class EmployeeControllerFacade {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final PositionMapper positionMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;

    @Autowired
    public EmployeeControllerFacade(EmployeeService employeeService,
                                    DepartmentService departmentService,
                                    PositionService positionService,
                                    PositionMapper positionMapper,
                                    EmployeeMapper employeeMapper, EmployeeValidator employeeValidator) {
        this.employeeService = employeeService;
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

    public List<EmployeeDTO> getEmployeeDTOList(Long companyBranchId, Long departmentId) {
        return employeeMapper.toDtoList(getEmployees(companyBranchId, departmentId));
    }

    private List<Employee> getEmployees(Long companyBranchId, Long departmentId) {
        return employeeService.getEmployeesByCompanyBranchAndDepartment(companyBranchId, departmentId);
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
}
