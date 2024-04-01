package com.manageemployee.employeemanagement.position.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentMapper;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.position.dto.mapper.PositionMapper;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.position.service.PositionService;
import com.manageemployee.employeemanagement.position.validation.PositionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
public class PositionControllerFacade {
    private final PositionService positionService;
    private final DepartmentService departmentService;
    private final PositionMapper positionMapper;
    private final PositionValidator positionValidator;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public PositionControllerFacade(PositionService positionService,
                                    DepartmentService departmentService,
                                    PositionMapper positionMapper,
                                    PositionValidator positionValidator,
                                    DepartmentMapper departmentMapper) {
        this.positionService = positionService;
        this.departmentService = departmentService;
        this.positionMapper = positionMapper;
        this.positionValidator = positionValidator;
        this.departmentMapper = departmentMapper;
    }

    public List<PositionDTO> getPositionDTOList(String departmentName, String isLeading) {
        Department department = departmentService.getByName(departmentName).orElse(null);
        List<Position> positions = positionService.getAllPositions(department, isLeading);
        return positionMapper.toDtoList(positions);
    }

    public List<DepartmentDTO> getDepartmentDTOList() {
        return departmentMapper.toDtoList(departmentService.getAllDepartments());
    }

    public void validate(PositionDTO position, BindingResult bindingResult) {
        positionValidator.validate(position, bindingResult);
    }

    public void savePosition(PositionDTO dto) {
        positionService.savePosition(positionMapper.toEntity(dto));
    }

    public PositionDTO getPositionDTO(Long positionId) {
        return positionMapper.toDto(positionService.getById(positionId));
    }

    public DepartmentDTO getDepartmentDTOFromPosition(Long positionId) {
        Position position = positionService.getById(positionId);
        return departmentMapper.toDto(position.getDepartment());
    }
}
