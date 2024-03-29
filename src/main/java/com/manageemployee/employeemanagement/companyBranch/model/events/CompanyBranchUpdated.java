package com.manageemployee.employeemanagement.companyBranch.model.events;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;

@Data
public class CompanyBranchUpdated {
    private final CompanyBranch companyBranch;
    private final Money oldBudget;
}
