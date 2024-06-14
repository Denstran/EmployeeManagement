package com.manageemployee.employeemanagement.employee.event.employeeEvent;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class EmployeeFired {
    private final Money oldSalary;
    private final CompanyBranchDepartmentPK departmentInfoPK;
    private final User user;
}
