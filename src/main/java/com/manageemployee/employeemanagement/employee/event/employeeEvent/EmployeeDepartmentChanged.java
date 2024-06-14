package com.manageemployee.employeemanagement.employee.event.employeeEvent;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;

@Data
public class EmployeeDepartmentChanged {
    private final String employeeContacts;
    private final Money employeeSalary;
    private final CompanyBranch companyBranch;
    private final Department oldDepartment;
    private final Department newDepartment;

    public CompanyBranchDepartmentPK getNewDepartmentInfoPK() {
        return new CompanyBranchDepartmentPK(companyBranch, newDepartment);
    }

    public CompanyBranchDepartmentPK getOldDepartmentInfoPK() {
        return new CompanyBranchDepartmentPK(companyBranch, oldDepartment);
    }
}
