package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeBaseEvent;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RemoveHrRoleProcessor implements RemoveRoleProcessor {
    @Override
    public void processRolesRemoval(EmployeeBaseEvent event) {
        Position position = event.getEmployee().getPosition();
        User user = event.getEmployee().getUser();
        if (!position.getDepartment().getDepartmentType().equals(DepartmentType.HR))
            user.removeRole(UserRole.ROLE_HR);
    }
}
