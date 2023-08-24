package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DepartmentMapper extends AbstractMapperWithSpecificFields<Department, DepartmentDTO> {
    private final CompanyBranchService companyBranchService;

    @Autowired
    public DepartmentMapper(ModelMapper modelMapper, CompanyBranchService companyBranchService) {
        super(Department.class, DepartmentDTO.class, modelMapper);
        this.companyBranchService = companyBranchService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Department.class, DepartmentDTO.class)
                .addMappings(m -> m.skip(DepartmentDTO::setCompanyBranchId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(DepartmentDTO.class, Department.class)
                .addMappings(m -> m.skip(Department::setCompanyBranch)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFieldsForDto(Department source, DepartmentDTO destination) {
        destination.setCompanyBranchId(Objects.isNull(source) ||
                Objects.isNull(source.getId()) ? null : source.getId());
    }

    @Override
    public void mapSpecificFieldsForEntity(DepartmentDTO source, Department destination) {
        destination.setCompanyBranch(companyBranchService.getCompanyBranchById(source.getCompanyBranchId()));
    }


}
