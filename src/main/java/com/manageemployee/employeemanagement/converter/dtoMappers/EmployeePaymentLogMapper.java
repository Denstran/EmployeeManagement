package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.EmployeePaymentLogDto;
import com.manageemployee.employeemanagement.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EmployeePaymentLogMapper
        extends AbstractMapperWithSpecificFields<EmployeePaymentLog, EmployeePaymentLogDto> {

    private final EmployeeService employeeService;

    public EmployeePaymentLogMapper(ModelMapper mapper, EmployeeService employeeService) {
        super(EmployeePaymentLog.class, EmployeePaymentLogDto.class, mapper);
        this.employeeService = employeeService;
    }

    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(EmployeePaymentLog.class, EmployeePaymentLogDto.class)
                .addMappings(m -> {
                    m.skip(EmployeePaymentLogDto::setEmployeeId);
                    m.skip(EmployeePaymentLogDto::setEmployeeName);
                    m.skip(EmployeePaymentLogDto::setEmployeePhoneNumber);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(EmployeePaymentLogDto.class, EmployeePaymentLog.class)
                .addMappings(m -> m.skip(EmployeePaymentLog::setEmployee)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFieldsForDto(EmployeePaymentLog source, EmployeePaymentLogDto destination) {
        destination.setEmployeeId(Objects.isNull(source) ? null : source.getEmployee().getId());
        destination.setEmployeeName(source.getEmployee().getName().toString());
        destination.setEmployeePhoneNumber(source.getEmployee().getPhoneNumber());
    }

    @Override
    protected void mapSpecificFieldsForEntity(EmployeePaymentLogDto source, EmployeePaymentLog destination) {
        destination.setEmployee(Objects.isNull(source) ? null : employeeService.getById(source.getEmployeeId()));
    }
}
