package com.manageemployee.employeemanagement.department.model.events;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.util.Money;

public class DepartmentInfoRemoved extends DepartmentInfoBaseEvent {

    public DepartmentInfoRemoved(CompanyBranch companyBranch, Department department,
                                 Money oldBudget, Money newBudget) {
        super(companyBranch, department, oldBudget, newBudget);
    }
}
