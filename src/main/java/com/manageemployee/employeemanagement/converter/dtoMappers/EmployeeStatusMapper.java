package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.EmployeeStatusDTO;
import com.manageemployee.employeemanagement.model.EmployeeStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeStatusMapper extends AbstractMapper<EmployeeStatus, EmployeeStatusDTO> {

    @Autowired
    public EmployeeStatusMapper(ModelMapper mapper) {
        super(EmployeeStatus.class, EmployeeStatusDTO.class, mapper);
    }
}
