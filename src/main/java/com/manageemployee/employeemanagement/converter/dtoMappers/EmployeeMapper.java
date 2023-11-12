package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.service.DepartmentService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation of AbstractMapperWithSpecificFields for Employee and EmployeeDTO
 */
@Component
public class EmployeeMapper extends AbstractMapperWithSpecificFields<Employee, EmployeeDTO> {
    private final DepartmentService departmentService;

    @Autowired
    public EmployeeMapper(ModelMapper mapper, DepartmentService departmentService) {
        super(Employee.class, EmployeeDTO.class, mapper);
        this.departmentService = departmentService;
    }

    /**
     * Set up method for creating specific type maps for model mapper for mapping specific fields
     */
    @PostConstruct
    public void setUpMapper() {
        mapper.createTypeMap(Employee.class, EmployeeDTO.class)
                .addMappings(m -> m.skip(EmployeeDTO::setDepartmentId)).setPostConverter(toDtoConverter());

        mapper.createTypeMap(EmployeeDTO.class, Employee.class)
                .addMappings(m -> m.skip(Employee::setDepartment)).setPostConverter(toEntityConverter());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForDto(Object, Object)
     * @param source - entity object, which will be mapped to dto
     * @param destination - dto object
     */
    @Override
    void mapSpecificFieldsForDto(Employee source, EmployeeDTO destination) {
        destination.setDepartmentId(Objects.isNull(source) ||
                Objects.isNull(source.getDepartment()) ? null : source.getDepartment().getId());

    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForEntity(Object, Object)
     * @param source - dto object, which will be mapped to entity
     * @param destination - entity object
     */
    @Override
    void mapSpecificFieldsForEntity(EmployeeDTO source, Employee destination) {
        destination.setDepartment(Objects.isNull(source.getDepartmentId()) ? null :
                departmentService.getDepartmentById(source.getDepartmentId()));
    }
}
