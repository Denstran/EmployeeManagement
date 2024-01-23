package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation of AbstractMapper for Department and DepartmentDTO
 */
@Component
public class DepartmentMapper extends AbstractMapper<Department, DepartmentDTO> {

    @Autowired
    public DepartmentMapper(ModelMapper modelMapper) {
        super(Department.class, DepartmentDTO.class, modelMapper);
    }

}
