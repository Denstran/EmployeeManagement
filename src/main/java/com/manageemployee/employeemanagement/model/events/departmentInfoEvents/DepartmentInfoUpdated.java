package com.manageemployee.employeemanagement.model.events.departmentInfoEvents;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;


public class DepartmentInfoUpdated extends DepartmentInfoBaseEvent {

    public DepartmentInfoUpdated(CompanyBranch companyBranch, Department department,
                                 Money oldBudget, Money newBudget) {
        super(companyBranch, department, oldBudget, newBudget);
    }
}
