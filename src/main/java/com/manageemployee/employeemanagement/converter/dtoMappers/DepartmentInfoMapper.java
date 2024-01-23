package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.DepartmentService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;

import java.util.Objects;

public class DepartmentInfoMapper extends AbstractMapperWithSpecificFields<DepartmentInfo, DepartmentInfoDTO> {

    private final CompanyBranchService companyBranchService;
    private final DepartmentService departmentService;

    public DepartmentInfoMapper(ModelMapper mapper, CompanyBranchService companyBranchService,
                                DepartmentService departmentService) {
        super(DepartmentInfo.class, DepartmentInfoDTO.class, mapper);
        this.companyBranchService = companyBranchService;
        this.departmentService = departmentService;
    }

    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(DepartmentInfo.class, DepartmentInfoDTO.class)
                .addMappings(m -> {
                    m.skip(DepartmentInfoDTO::setDepartmentId);
                    m.skip(DepartmentInfoDTO::setCompanyBranchId);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(DepartmentInfoDTO.class, DepartmentInfo.class)
                .addMappings(m -> m.skip(DepartmentInfo::setPk)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFieldsForDto(DepartmentInfo source, DepartmentInfoDTO destination) {
        if (Objects.isNull(source) || Objects.isNull(source.getPk())) {
            destination.setDepartmentId(null);
            destination.setCompanyBranchId(null);
        }else {
            destination.setDepartmentId(source.getPk().getDepartment().getId());
            destination.setCompanyBranchId(source.getPk().getCompanyBranch().getId());
        }
    }

    @Override
    protected void mapSpecificFieldsForEntity(DepartmentInfoDTO source, DepartmentInfo destination) {
        if (Objects.isNull(source) || Objects.isNull(source.getDepartmentId()) ||
                Objects.isNull(source.getCompanyBranchId())) destination.setPk(null);
        else {
            CompanyBranch cbRef = companyBranchService.getReference(source.getCompanyBranchId());
            Department depRef = departmentService.getReference(source.getDepartmentId());

            CompanyBranchDepartmentPK pk = new CompanyBranchDepartmentPK(cbRef, depRef);
            destination.setPk(pk);
        }
    }
}
