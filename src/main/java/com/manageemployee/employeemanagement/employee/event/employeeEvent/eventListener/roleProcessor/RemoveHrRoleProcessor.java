package com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RemoveHrRoleProcessor implements RemoveRoleProcessor {
    @Override
    public void processRolesRemoval(RoleHolder roleHolder) {
        Position position = roleHolder.getPosition();
        User user = roleHolder.getUser();
        if (!position.getDepartment().getDepartmentType().equals(DepartmentType.HR))
            user.removeRole(UserRole.ROLE_HR);
    }
}
