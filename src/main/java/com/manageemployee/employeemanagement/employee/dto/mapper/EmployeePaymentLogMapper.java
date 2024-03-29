package com.manageemployee.employeemanagement.employee.dto.mapper;

import com.manageemployee.employeemanagement.employee.dto.EmployeePaymentLogDTO;
import com.manageemployee.employeemanagement.employee.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapperWithSpecificFields;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EmployeePaymentLogMapper
        extends AbstractMapperWithSpecificFields<EmployeePaymentLog, EmployeePaymentLogDTO> {

    private final EmployeeService employeeService;

    public EmployeePaymentLogMapper(ModelMapper mapper, EmployeeService employeeService) {
        super(EmployeePaymentLog.class, EmployeePaymentLogDTO.class, mapper);
        this.employeeService = employeeService;
    }

    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(EmployeePaymentLog.class, EmployeePaymentLogDTO.class)
                .addMappings(m -> {
                    m.skip(EmployeePaymentLogDTO::setEmployeeId);
                    m.skip(EmployeePaymentLogDTO::setEmployeeName);
                    m.skip(EmployeePaymentLogDTO::setEmployeePhoneNumber);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(EmployeePaymentLogDTO.class, EmployeePaymentLog.class)
                .addMappings(m -> m.skip(EmployeePaymentLog::setEmployee)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFieldsForDto(EmployeePaymentLog source, EmployeePaymentLogDTO destination) {
        destination.setEmployeeId(Objects.isNull(source) ? null : source.getEmployee().getId());
        destination.setEmployeeName(source.getEmployee().getName().toString());
        destination.setEmployeePhoneNumber(source.getEmployee().getPhoneNumber());
    }

    @Override
    protected void mapSpecificFieldsForEntity(EmployeePaymentLogDTO source, EmployeePaymentLog destination) {
        destination.setEmployee(Objects.isNull(source) ? null : employeeService.getById(source.getEmployeeId()));
    }
}
