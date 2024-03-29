package com.manageemployee.employeemanagement.department.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.util.Money;

public interface EmployeeService {
    void deleteAllByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department);
    Money countEmployeeSalariesByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department);
}
