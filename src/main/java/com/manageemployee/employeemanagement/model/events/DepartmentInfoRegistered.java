package com.manageemployee.employeemanagement.model.events;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public class DepartmentInfoRegistered {
    private final Money departmentBudget;
    private final CompanyBranch companyBranch;

}
