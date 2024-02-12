package com.manageemployee.employeemanagement.model.events;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public class DepartmentInfoRemoved {
    private final CompanyBranch companyBranch;
    private final Money departmentBudget;
}
