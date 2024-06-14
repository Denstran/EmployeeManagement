package com.manageemployee.employeemanagement.employee.event.employeeEvent;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;

@Data
public class EmployeeSalaryChanged {
    private final Long employeeId;
    private final Money oldSalary;
    private final Money newSalary;
    private final CompanyBranchDepartmentPK departmentInfoPK;
    private final Department oldDepartment;
}
