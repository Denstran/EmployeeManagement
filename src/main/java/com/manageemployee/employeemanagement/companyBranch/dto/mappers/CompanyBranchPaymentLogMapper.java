package com.manageemployee.employeemanagement.companyBranch.dto.mappers;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchPaymentLogDto;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapperWithSpecificFields;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CompanyBranchPaymentLogMapper
        extends AbstractMapperWithSpecificFields<CompanyBranchPaymentLog, CompanyBranchPaymentLogDto> {

    private final CompanyBranchService companyBranchService;

    @Autowired
    public CompanyBranchPaymentLogMapper(ModelMapper mapper, CompanyBranchService companyBranchService) {
        super(CompanyBranchPaymentLog.class, CompanyBranchPaymentLogDto.class, mapper);
        this.companyBranchService = companyBranchService;
    }

    @Override
    public void setupMapper() {
        mapper.createTypeMap(CompanyBranchPaymentLog.class, CompanyBranchPaymentLogDto.class)
                .addMappings(m -> m.skip(CompanyBranchPaymentLogDto::setCompanyBranchId));

        mapper.createTypeMap(CompanyBranchPaymentLogDto.class, CompanyBranchPaymentLog.class)
                .addMappings(m -> m.skip(CompanyBranchPaymentLog::setCompanyBranch));
    }

    @Override
    protected void mapSpecificFieldsForDto(CompanyBranchPaymentLog source, CompanyBranchPaymentLogDto destination) {
        destination.setCompanyBranchId(Objects.isNull(source) ||
                Objects.isNull(source.getCompanyBranch()) ? null : source.getCompanyBranch().getId());
    }

    @Override
    protected void mapSpecificFieldsForEntity(CompanyBranchPaymentLogDto source, CompanyBranchPaymentLog destination) {
        destination.setCompanyBranch(Objects.isNull(source) || Objects.isNull(source.getCompanyBranchId()) ? null :
                companyBranchService.getReference(source.getCompanyBranchId()));
    }
}
