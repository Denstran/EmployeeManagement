package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeBaseEvent;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import org.springframework.stereotype.Component;

@Component
public class AddHrRoleProcessor implements AddRoleProcessor {
    @Override
    public void processAddRole(EmployeeBaseEvent event) {
        User user = event.getEmployee().getUser();
        Position position = event.getEmployee().getPosition();
        if (position.getDepartment().getDepartmentType().equals(DepartmentType.HR))
            user.addRole(UserRole.ROLE_HR);
    }
}
