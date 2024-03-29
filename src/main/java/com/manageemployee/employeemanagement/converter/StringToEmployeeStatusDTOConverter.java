package com.manageemployee.employeemanagement.converter;

import com.manageemployee.employeemanagement.model.enumTypes.EmployeeStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Class for converting String to EmployeeStatusDTO
 */
@Component
public class StringToEmployeeStatusDTOConverter implements Converter<String, EmployeeStatus> {

    @Override
    public EmployeeStatus convert(String source) {

        return EmployeeStatus.valueOf(source);
    }
}
