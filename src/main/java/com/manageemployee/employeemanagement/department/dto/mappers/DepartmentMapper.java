package com.manageemployee.employeemanagement.department.dto.mappers;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
