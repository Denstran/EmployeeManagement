package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor;

import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeBaseEvent;

public interface RemoveRoleProcessor {
    void processRolesRemoval(EmployeeBaseEvent event);
}
