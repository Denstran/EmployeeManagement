package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeBaseEvent;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RemoveHeadOfDepartmentRoleProcessor implements RemoveRoleProcessor {
    @Override
    public void processRolesRemoval(EmployeeBaseEvent event) {
        User user = event.getEmployee().getUser();
        Position position = event.getEmployee().getPosition();
        if (!position.isLeading())
            user.removeRole(UserRole.ROLE_HEAD_OF_DEPARTMENT);
    }
}
