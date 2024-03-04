package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentDeleted;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import com.manageemployee.employeemanagement.service.PositionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DepartmentEventListener {

    private final DepartmentInfoService departmentInfoService;
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final PaymentLogService paymentLogService;

    public DepartmentEventListener(DepartmentInfoService departmentInfoService,
                                   EmployeeService employeeService, PositionService positionService, PaymentLogService paymentLogService) {
        this.departmentInfoService = departmentInfoService;
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.paymentLogService = paymentLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentDeletedEventHandler(DepartmentDeleted departmentDeleted) {
        Department department = departmentDeleted.getDepartment();

        positionService.deleteByDepartment(department);
        departmentInfoService.deleteAllByDepartment(department);
    }
}
