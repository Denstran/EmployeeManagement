package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.service.DepartmentService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EmployeeMapper extends AbstractMapperWithSpecificFields<Employee, EmployeeDTO> {
    private final DepartmentService departmentService;

    @Autowired
    public EmployeeMapper(ModelMapper mapper, DepartmentService departmentService) {
        super(Employee.class, EmployeeDTO.class, mapper);
        this.departmentService = departmentService;
    }

    @PostConstruct
    public void setUpMapper() {
        mapper.createTypeMap(Employee.class, EmployeeDTO.class)
                .addMappings(m -> m.skip(EmployeeDTO::setDepartmentId)).setPostConverter(toDtoConverter());

        mapper.createTypeMap(EmployeeDTO.class, Employee.class)
                .addMappings(m -> m.skip(Employee::setDepartment)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFieldsForDto(Employee source, EmployeeDTO destination) {
        destination.setDepartmentId(Objects.isNull(source) ||
                Objects.isNull(source.getDepartment()) ? null : source.getDepartment().getId());

    }

    @Override
    void mapSpecificFieldsForEntity(EmployeeDTO source, Employee destination) {
        destination.setDepartment(Objects.isNull(source.getDepartmentId()) ? null :
                departmentService.getDepartmentById(source.getDepartmentId()));
    }
}
