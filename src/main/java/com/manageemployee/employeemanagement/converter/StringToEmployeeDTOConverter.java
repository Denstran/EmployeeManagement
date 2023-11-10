package com.manageemployee.employeemanagement.converter;

import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeStatusMapper;
import com.manageemployee.employeemanagement.dto.EmployeeStatusDTO;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.service.EmployeeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEmployeeDTOConverter implements Converter<String, EmployeeStatusDTO> {
    private final EmployeeStatusService employeeStatusService;
    private final EmployeeStatusMapper employeeStatusMapper;

    @Autowired
    public StringToEmployeeDTOConverter(EmployeeStatusService employeeStatusService, EmployeeStatusMapper employeeStatusMapper) {
        this.employeeStatusService = employeeStatusService;
        this.employeeStatusMapper = employeeStatusMapper;
    }

    @Override
    public EmployeeStatusDTO convert(String source) {
        EEmployeeStatus employeeStatus = EEmployeeStatus.valueOf(source);

        return employeeStatusMapper.toDto(employeeStatusService.getStatusByName(employeeStatus));
    }
}
