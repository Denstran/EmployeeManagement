package com.manageemployee.employeemanagement.employee.event.employeeEvent;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor.RoleHolder;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import lombok.Data;

@Data
public class EmployeePositionChanged implements RoleHolder {
    private final Position oldPosition;
    private final Position newPosition;
    private final User user;
    private final String email;
    private final CompanyBranch companyBranch;

    public CompanyBranchDepartmentPK getNewDepartmentInfoPK() {
        return new CompanyBranchDepartmentPK(companyBranch, newPosition.getDepartment());
    }

    public CompanyBranchDepartmentPK getOldDepartmentInfoPK() {
        return new CompanyBranchDepartmentPK(companyBranch, oldPosition.getDepartment());
    }

    @Override
    public Position getPosition() {
        return this.newPosition;
    }

    @Override
    public User getUser() {
        return this.user;
    }
}
