package com.manageemployee.employeemanagement.model.events.companyBranchEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import lombok.Data;

@Data
public class CompanyBranchDeleted {
    private final CompanyBranch companyBranch;
}
