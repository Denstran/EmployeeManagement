package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentDeleted;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.PositionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DepartmentEventListener {

    private final DepartmentInfoService departmentInfoService;
    private final PositionService positionService;

    public DepartmentEventListener(DepartmentInfoService departmentInfoService,
                                   PositionService positionService) {
        this.departmentInfoService = departmentInfoService;
        this.positionService = positionService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentDeletedEventHandler(DepartmentDeleted departmentDeleted) {
        Department department = departmentDeleted.getDepartment();

        positionService.deleteByDepartment(department);
        departmentInfoService.deleteAllByDepartment(department);
    }
}
