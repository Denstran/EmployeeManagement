package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeBaseEvent;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RemoveAccountingRoleProcessor implements RemoveRoleProcessor {

    @Override
    public void processRolesRemoval(EmployeeBaseEvent event) {
        User user = event.getEmployee().getUser();
        Position position = event.getEmployee().getPosition();
        if (!position.getDepartment().getDepartmentType().equals(DepartmentType.ACCOUNTING))
            user.removeRole(UserRole.ROLE_ACCOUNTING);
    }
}
