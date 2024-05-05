package com.manageemployee.employeemanagement.employee.dto.mapper;

import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapperWithSpecificFields;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VacationRequestMapper extends AbstractMapperWithSpecificFields<VacationRequest, VacationRequestDTO> {
    private final EmployeeService employeeService;

    @Autowired
    public VacationRequestMapper(ModelMapper mapper, EmployeeService employeeService) {
        super(VacationRequest.class, VacationRequestDTO.class, mapper);
        this.employeeService = employeeService;
    }

    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(VacationRequest.class, VacationRequestDTO.class)
                .addMappings(m -> m.skip(VacationRequestDTO::setEmployeeId))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(VacationRequestDTO.class, VacationRequest.class)
                .addMappings(m -> m.skip(VacationRequest::setEmployee))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFieldsForDto(VacationRequest source, VacationRequestDTO destination) {
        log.info("MAPPING VACATION_REQUEST OBJ: {} TO DTO", source);
        destination.setEmployeeId(source.getEmployee().getId());
        log.info("MAPPING RESULT: {}", source);
    }

    @Override
    protected void mapSpecificFieldsForEntity(VacationRequestDTO source, VacationRequest destination) {
        Employee employee = employeeService.getReference(source.getEmployeeId());
        destination.setEmployee(employee);
    }
}
