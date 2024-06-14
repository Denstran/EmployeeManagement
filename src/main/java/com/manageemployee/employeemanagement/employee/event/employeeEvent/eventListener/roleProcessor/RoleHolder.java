package com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;

public interface RoleHolder {
    User getUser();
    Position getPosition();
}
