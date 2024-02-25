package com.manageemployee.employeemanagement.model.events.employeeEvents;


import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;
import lombok.Getter;

@Getter
public class EmployeeUpdated extends EmployeeBaseEvent{
    private final Money oldSalary;

    public EmployeeUpdated(Money salary, Department department, CompanyBranch companyBranch, Money oldSalary) {
        super(salary, department, companyBranch);
        this.oldSalary = oldSalary;
    }
}
