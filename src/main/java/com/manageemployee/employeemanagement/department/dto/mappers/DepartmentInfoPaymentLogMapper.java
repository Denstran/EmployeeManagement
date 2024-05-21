package com.manageemployee.employeemanagement.department.dto.mappers;

import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoPaymentLogDTO;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapperWithSpecificFields;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class DepartmentInfoPaymentLogMapper
        extends AbstractMapperWithSpecificFields<DepartmentInfoPaymentLog, DepartmentInfoPaymentLogDTO> {

    private final CompanyBranchService companyBranchService;
    private final DepartmentService departmentService;

    public DepartmentInfoPaymentLogMapper(ModelMapper mapper, CompanyBranchService companyBranchService,
                                          DepartmentService departmentService) {
        super(DepartmentInfoPaymentLog.class, DepartmentInfoPaymentLogDTO.class, mapper);
        this.companyBranchService = companyBranchService;
        this.departmentService = departmentService;
    }

    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(DepartmentInfoPaymentLog.class, DepartmentInfoPaymentLogDTO.class)
                .addMappings(m -> {
                    m.skip(DepartmentInfoPaymentLogDTO::setDepartmentId);
                    m.skip(DepartmentInfoPaymentLogDTO::setCompanyBranchId);
                    m.skip(DepartmentInfoPaymentLogDTO::setDepartmentName);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(DepartmentInfoPaymentLogDTO.class, DepartmentInfoPaymentLog.class)
                .addMappings(m -> {
                    m.skip(DepartmentInfoPaymentLog::setCompanyBranch);
                    m.skip(DepartmentInfoPaymentLog::setDepartment);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFieldsForDto(DepartmentInfoPaymentLog source, DepartmentInfoPaymentLogDTO destination) {
        destination.setCompanyBranchId(Objects.isNull(source) ||
                Objects.isNull(source.getCompanyBranch()) ? null : source.getCompanyBranch().getId());
        destination.setDepartmentId(Objects.isNull(source) ||
                Objects.isNull(source.getDepartment()) ? null : source.getDepartment().getId());
        destination.setDepartmentName(source.getDepartment().getDepartmentName());
    }

    @Override
    protected void mapSpecificFieldsForEntity(DepartmentInfoPaymentLogDTO source, DepartmentInfoPaymentLog destination) {
        destination.setCompanyBranch(Objects.isNull(source.getCompanyBranchId()) ? null :
                companyBranchService.getReference(source.getCompanyBranchId()));

        destination.setDepartment(Objects.isNull(source.getDepartmentId()) ?
                null : departmentService.getReference(source.getDepartmentId()));
    }
}
