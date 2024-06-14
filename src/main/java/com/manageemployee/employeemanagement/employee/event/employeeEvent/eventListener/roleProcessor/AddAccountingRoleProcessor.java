package com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import org.springframework.stereotype.Component;

@Component
public class AddAccountingRoleProcessor implements AddRoleProcessor {
    @Override
    public void processAddRole(RoleHolder roleHolder) {
        User user = roleHolder.getUser();
        Position position = roleHolder.getPosition();
        if (position.getDepartment().getDepartmentType().equals(DepartmentType.ACCOUNTING))
            user.addRole(UserRole.ROLE_ACCOUNTING);
    }
}
