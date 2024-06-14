package com.manageemployee.employeemanagement.employee.event.employeeEvent;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Getter;

@Getter
public class EmployeeHired {
    private final String password;
    private final Employee employee;
    public EmployeeHired(String password, Employee employee) {
        this.password = password;
        this.employee = employee;
    }

    public String getEmail() {
        return employee.getEmail();
    }

    public CompanyBranch getCompanyBranch() {
        return employee.getCompanyBranch();
    }

    public Department getDepartment() {
        return employee.getPosition().getDepartment();
    }

    public CompanyBranchDepartmentPK getDepartmentInfoPK() {
        return new CompanyBranchDepartmentPK(getCompanyBranch(), getDepartment());
    }

    public Money getSalary() {
        return employee.getSalary();
    }
}
