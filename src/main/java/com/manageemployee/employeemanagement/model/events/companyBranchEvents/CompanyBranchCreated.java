package com.manageemployee.employeemanagement.model.events.companyBranchEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import lombok.Data;

@Data
public class CompanyBranchCreated {
    private final CompanyBranch companyBranch;
}
