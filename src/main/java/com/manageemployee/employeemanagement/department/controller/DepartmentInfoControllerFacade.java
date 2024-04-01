package com.manageemployee.employeemanagement.department.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentInfoMapper;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentMapper;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.department.validation.departmentInfoValidation.DepartmentInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
public class DepartmentInfoControllerFacade {
    private final DepartmentInfoMapper departmentInfoMapper;
    private final DepartmentInfoValidator departmentInfoValidator;
    private final DepartmentInfoService departmentInfoService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentInfoControllerFacade(DepartmentInfoMapper departmentInfoMapper,
                                          DepartmentInfoValidator departmentInfoValidator,
                                          DepartmentInfoService departmentInfoService,
                                          DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentInfoMapper = departmentInfoMapper;
        this.departmentInfoValidator = departmentInfoValidator;
        this.departmentInfoService = departmentInfoService;
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    public List<DepartmentInfoDTO> getDepartmentInfoDTOS(Long companyBranchId) {
        return departmentInfoMapper.toDtoList(getDepartmentsInfo(companyBranchId));
    }

    private List<DepartmentInfo> getDepartmentsInfo(Long companyBranchId) {
        return departmentInfoService.getDepartmentsByCompanyBranchId(companyBranchId);
    }

    public List<DepartmentDTO> getAvailableDepartmentsDTO(Long companyBranchId) {
        return departmentMapper.toDtoList(getAvailableDepartments(companyBranchId));
    }

    public DepartmentDTO getDepartmentDTOFromDepartmentInfo(Long companyBranchId, Long departmentId) {
        DepartmentInfo departmentInfo = getDepartmentInfoById(companyBranchId, departmentId);
        return departmentMapper.toDto(departmentInfo.getPk().getDepartment());
    }

    public DepartmentInfoDTO getDepartmentInfoDTO(Long companyBranchId, Long departmentId) {
        return departmentInfoMapper.toDto(getDepartmentInfoById(companyBranchId, departmentId));
    }

    private DepartmentInfo getDepartmentInfoById(Long companyBranchId, Long departmentId) {
        return departmentInfoService.getById(companyBranchId, departmentId);
    }

    private List<Department> getAvailableDepartments(Long companyBranchId) {
        return departmentService.getAvailableDepartments(companyBranchId);
    }

    public void validate(DepartmentInfoDTO departmentInfo, BindingResult bindingResult) {
        departmentInfoValidator.validate(departmentInfo, bindingResult);
    }

    private DepartmentInfo toEntity(DepartmentInfoDTO dto) {
        return departmentInfoMapper.toEntity(dto);
    }

    public void createDepartmentInfo(DepartmentInfoDTO dto) {
        departmentInfoService.create(toEntity(dto));
    }

    public void updateDepartmentInfo(DepartmentInfoDTO dto) {
        departmentInfoService.update(toEntity(dto));
    }

    public void removeDepartment(Long companyBranchId, Long departmentId) {
        DepartmentInfo departmentInfo = departmentInfoService.getById(companyBranchId, departmentId);
        departmentInfoService.deleteDepartmentInfo(departmentInfo);
    }
}
