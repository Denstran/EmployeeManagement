package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.PositionService;
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
    private final CompanyBranchService companyBranchService;
    private final PositionService positionService;

    @Autowired
    public EmployeeMapper(ModelMapper mapper, CompanyBranchService companyBranchService,
                          PositionService positionService) {
        super(Employee.class, EmployeeDTO.class, mapper);
        this.companyBranchService = companyBranchService;
        this.positionService = positionService;
    }

    /**
     * Set up method for creating specific type maps for model mapper for mapping specific fields
     */
    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(Employee.class, EmployeeDTO.class)
                .addMappings(m -> {
                    m.skip(EmployeeDTO::setCompanyBranchId);
                    m.skip(EmployeeDTO::setPositionId);
                    m.skip(EmployeeDTO::setPositionName);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(EmployeeDTO.class, Employee.class)
                .addMappings(m -> {
                    m.skip(Employee::setCompanyBranch);
                    m.skip(Employee::setPosition);
                }).setPostConverter(toEntityConverter());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForDto(Object, Object)
     * @param source - entity object, which will be mapped to dto
     * @param destination - dto object
     */
    @Override
    protected void mapSpecificFieldsForDto(Employee source, EmployeeDTO destination) {
        destination.setCompanyBranchId(Objects.isNull(source) ||
                Objects.isNull(source.getCompanyBranch()) ? null : source.getCompanyBranch().getId());

        destination.setPositionId(Objects.isNull(source) ||
                Objects.isNull(source.getPosition()) ? null : source.getPosition().getId());

        destination.setPositionName(source.getPosition().getPositionName());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForEntity(Object, Object)
     * @param source - dto object, which will be mapped to entity
     * @param destination - entity object
     */
    @Override
    protected void mapSpecificFieldsForEntity(EmployeeDTO source, Employee destination) {
        destination.setCompanyBranch(Objects.isNull(source.getCompanyBranchId()) ? null :
                companyBranchService.getReference(source.getCompanyBranchId()));

        destination.setPosition(Objects.isNull(source.getPositionId()) ? null :
                positionService.getReference(source.getPositionId()));
    }
}
