package com.manageemployee.employeemanagement.companyBranch.model.events;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import lombok.Data;

@Data
public class CompanyBranchDeleted {
    private final CompanyBranch companyBranch;
}
