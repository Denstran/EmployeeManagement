package com.manageemployee.employeemanagement.companyBranch.dto.mappers;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of AbstractMapper for CompanyBranch and CompanyBranchDTO
 */
@Component
public class CompanyBranchMapper extends AbstractMapper<CompanyBranch, CompanyBranchDTO> {

    @Autowired
    public CompanyBranchMapper(ModelMapper mapper) {
        super(CompanyBranch.class, CompanyBranchDTO.class, mapper);
    }
}
