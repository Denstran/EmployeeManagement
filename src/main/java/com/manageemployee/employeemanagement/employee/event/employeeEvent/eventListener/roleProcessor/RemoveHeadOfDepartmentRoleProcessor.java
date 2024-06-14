package com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RemoveHeadOfDepartmentRoleProcessor implements RemoveRoleProcessor {
    @Override
    public void processRolesRemoval(RoleHolder roleHolder) {
        User user = roleHolder.getUser();
        Position position = roleHolder.getPosition();
        if (!position.isLeading())
            user.removeRole(UserRole.ROLE_HEAD_OF_DEPARTMENT);
    }
}
