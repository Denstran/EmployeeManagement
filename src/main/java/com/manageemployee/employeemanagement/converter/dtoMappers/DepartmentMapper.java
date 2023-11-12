package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation of AbstractMapperWithSpecificFields for Department and DepartmentDTO
 */
@Component
public class DepartmentMapper extends AbstractMapperWithSpecificFields<Department, DepartmentDTO> {
    private final CompanyBranchService companyBranchService;

    @Autowired
    public DepartmentMapper(ModelMapper modelMapper, CompanyBranchService companyBranchService) {
        super(Department.class, DepartmentDTO.class, modelMapper);
        this.companyBranchService = companyBranchService;
    }

    /**
     * @see AbstractMapperWithSpecificFields#setupMapper()
     */
    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(Department.class, DepartmentDTO.class)
                .addMappings(m -> m.skip(DepartmentDTO::setCompanyBranchId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(DepartmentDTO.class, Department.class)
                .addMappings(m -> m.skip(Department::setCompanyBranch)).setPostConverter(toEntityConverter());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForDto(Object, Object)
     * @param source - entity object, which will be mapped to dto
     * @param destination - dto object
     */
    @Override
    protected void mapSpecificFieldsForDto(Department source, DepartmentDTO destination) {
        destination.setCompanyBranchId(Objects.isNull(source) ||
                Objects.isNull(source.getCompanyBranch()) ? null : source.getCompanyBranch().getId());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForEntity(Object, Object)
     * @param source - dto object, which will be mapped to entity
     * @param destination - entity object
     */
    @Override
    protected void mapSpecificFieldsForEntity(DepartmentDTO source, Department destination) {
        destination.setCompanyBranch(companyBranchService.getCompanyBranchById(source.getCompanyBranchId()));
    }


}
