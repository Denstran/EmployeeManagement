package com.manageemployee.employeemanagement.employee.event.employeeEvent;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor.RoleHolder;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.util.Money;
import lombok.Data;

@Data
public class EmployeeRestored implements RoleHolder {
    private final Employee employee;

    public CompanyBranchDepartmentPK getDepartmentInfoPK() {
        return new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment());
    }

    public Money getSalary() {
        return employee.getSalary();
    }

    @Override
    public User getUser() {
        return employee.getUser();
    }

    @Override
    public Position getPosition() {
        return employee.getPosition();
    }
}
