package com.manageemployee.employeemanagement.department.model.events;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.util.Money;


public class DepartmentInfoUpdated extends DepartmentInfoBaseEvent {

    public DepartmentInfoUpdated(CompanyBranch companyBranch, Department department,
                                 Money oldBudget, Money newBudget) {
        super(companyBranch, department, oldBudget, newBudget);
    }
}
