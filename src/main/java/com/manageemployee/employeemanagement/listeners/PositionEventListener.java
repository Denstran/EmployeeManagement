package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.model.events.positionEvents.PositionDeleted;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PositionEventListener {
    private final EmployeeService employeeService;
    private final PaymentLogService paymentLogService;

    @Autowired
    public PositionEventListener(EmployeeService employeeService, PaymentLogService paymentLogService) {
        this.employeeService = employeeService;
        this.paymentLogService = paymentLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void positionRemovedEventHandler(PositionDeleted positionDeleted) {
        Position position = positionDeleted.getPosition();
        paymentLogService.deleteEmployeesPaymentLogsByPosition(position);
        employeeService.deleteAllByPosition(position);
    }
}
