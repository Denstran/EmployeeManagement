package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.DepartmentInfoPaymentLogDto;
import com.manageemployee.employeemanagement.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DepartmentInfoPaymentLogMapper
        extends AbstractMapperWithSpecificFields<DepartmentInfoPaymentLog, DepartmentInfoPaymentLogDto> {

    private final CompanyBranchService companyBranchService;
    private final DepartmentService departmentService;

    public DepartmentInfoPaymentLogMapper(ModelMapper mapper, CompanyBranchService companyBranchService,
                                          DepartmentService departmentService) {
        super(DepartmentInfoPaymentLog.class, DepartmentInfoPaymentLogDto.class, mapper);
        this.companyBranchService = companyBranchService;
        this.departmentService = departmentService;
    }

    @Override
    public void setupMapper() {
        mapper.createTypeMap(DepartmentInfoPaymentLog.class, DepartmentInfoPaymentLogDto.class)
                .addMappings(m -> {
                    m.skip(DepartmentInfoPaymentLogDto::setDepartmentId);
                    m.skip(DepartmentInfoPaymentLogDto::setCompanyBranchId);
                });

        mapper.createTypeMap(DepartmentInfoPaymentLogDto.class, DepartmentInfoPaymentLog.class)
                .addMappings(m -> {
                    m.skip(DepartmentInfoPaymentLog::setCompanyBranch);
                    m.skip(DepartmentInfoPaymentLog::setDepartment);
                });
    }

    @Override
    protected void mapSpecificFieldsForDto(DepartmentInfoPaymentLog source, DepartmentInfoPaymentLogDto destination) {
        destination.setCompanyBranchId(Objects.isNull(source) ||
                Objects.isNull(source.getCompanyBranch()) ? null : source.getCompanyBranch().getId());
        destination.setDepartmentId(Objects.isNull(source) ||
                Objects.isNull(source.getDepartment()) ? null : source.getDepartment().getId());

    }

    @Override
    protected void mapSpecificFieldsForEntity(DepartmentInfoPaymentLogDto source, DepartmentInfoPaymentLog destination) {
        destination.setCompanyBranch(Objects.isNull(source.getCompanyBranchId()) ? null :
                companyBranchService.getReference(source.getCompanyBranchId()));

        destination.setDepartment(Objects.isNull(source.getDepartmentId()) ?
                null : departmentService.getReference(source.getDepartmentId()));
    }
}
