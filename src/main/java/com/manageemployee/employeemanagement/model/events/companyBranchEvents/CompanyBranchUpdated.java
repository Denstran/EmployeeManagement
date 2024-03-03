package com.manageemployee.employeemanagement.model.events.companyBranchEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public class CompanyBranchUpdated {
    private final CompanyBranch companyBranch;
    private final Money oldBudget;
}
