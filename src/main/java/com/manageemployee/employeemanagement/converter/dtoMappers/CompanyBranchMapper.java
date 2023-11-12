package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of AbstractMapper for CompanyBranch and CompanyBranchDTO
 */
@Component
public class CompanyBranchMapper extends AbstractMapper<CompanyBranch, CompanyBranchDTO>  {

    @Autowired
    public CompanyBranchMapper(ModelMapper mapper) {
        super(CompanyBranch.class, CompanyBranchDTO.class, mapper);
    }
}
