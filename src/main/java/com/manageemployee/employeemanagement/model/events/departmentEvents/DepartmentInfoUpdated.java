package com.manageemployee.employeemanagement.model.events.departmentEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Data;

@Data
public class DepartmentInfoUpdated {
    private final CompanyBranch companyBranch;
    private final Money oldDepartmentBudget;
    private final Money newDepartmentBudget;
}
