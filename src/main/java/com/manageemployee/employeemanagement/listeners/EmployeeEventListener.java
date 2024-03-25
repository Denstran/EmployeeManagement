package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.*;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeBaseEvent;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeFired;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeHired;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeUpdated;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmployeeEventListener {
    private final DepartmentInfoService departmentInfoService;
    private final PaymentLogService paymentLogService;

    @Autowired
    public EmployeeEventListener(DepartmentInfoService departmentInfoService, PaymentLogService paymentLogService) {
        this.departmentInfoService = departmentInfoService;
        this.paymentLogService = paymentLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createEmployeeEventHandler(EmployeeHired employeeHired) {
        processSalaryChanges(employeeHired, Action.CREATE);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateEmployeeEventHandler(EmployeeUpdated employeeUpdated) {
        processSalaryChanges(employeeUpdated, Action.UPDATE);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void fireEmployeeEventHandler(EmployeeFired employeeFired) {
        processSalaryChanges(employeeFired, Action.DELETE);
    }

    private void processSalaryChanges(EmployeeBaseEvent baseEvent, Action action) {
        switch (action) {
            case CREATE -> processEmployeeHiring(baseEvent);
            case UPDATE -> processSalaryUpdated(baseEvent);
            case DELETE -> processEmployeeRemoval(baseEvent);
        }
    }

    private void processEmployeeHiring(EmployeeBaseEvent baseEvent) {
        processEmployeePaymentLogCreation(baseEvent, baseEvent.getNewSalary(), true);
        processNegativeDepartmentBudgetChanges(getDepartmentInfo(baseEvent), baseEvent.getNewSalary());
    }

    private void processSalaryUpdated(EmployeeBaseEvent baseEvent) {
        Money oldSalary = baseEvent.getOldSalary();
        Money newSalary = baseEvent.getNewSalary();
        if (oldSalary.equals(newSalary)) return;

        if (isPositiveSalaryChanges(newSalary, oldSalary)) processPositiveSalaryChanges(baseEvent);
        else processNegativeSalaryChanges(baseEvent);
    }

    private void processNegativeSalaryChanges(EmployeeBaseEvent baseEvent) {
        Money salaryChanges = MoneyService.subtract(baseEvent.getOldSalary(), baseEvent.getNewSalary());
        processEmployeePaymentLogCreation(baseEvent, salaryChanges, false);
        processPositiveDepartmentBudgetChanges(getDepartmentInfo(baseEvent), salaryChanges);
    }

    private void processPositiveSalaryChanges(EmployeeBaseEvent baseEvent) {
        Money salaryChanges = MoneyService.subtract(baseEvent.getNewSalary(), baseEvent.getOldSalary());
        processEmployeePaymentLogCreation(baseEvent, salaryChanges, true);
        processNegativeDepartmentBudgetChanges(getDepartmentInfo(baseEvent), salaryChanges);
    }

    private void processNegativeDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.allocateBudgetForSalary(departmentInfo, budgetChanges);
        processDepartmentInfoPaymentLogCreation(departmentInfo, MoneyService.abs(budgetChanges), false);
    }

    private boolean isPositiveSalaryChanges(Money newSalary, Money oldSalary) {
        int compareResult = MoneyService.compareAmounts(newSalary, oldSalary);
        return compareResult == 1;
    }

    private void processEmployeeRemoval(EmployeeBaseEvent baseEvent) {
        DepartmentInfo departmentInfo = getDepartmentInfo(baseEvent);
        processPositiveDepartmentBudgetChanges(departmentInfo, baseEvent.getOldSalary());
    }

    private void processPositiveDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.employeeSalaryReducing(departmentInfo, budgetChanges);
        processDepartmentInfoPaymentLogCreation(departmentInfo, budgetChanges, true);
    }

    private void processDepartmentInfoPaymentLogCreation(DepartmentInfo departmentInfo, Money paymentAmount,
                                                         boolean isPositive) {
        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog
                .createPaymentLog(departmentInfo.getPk(), MoneyService.abs(paymentAmount), isPositive);
        paymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void processEmployeePaymentLogCreation(EmployeeBaseEvent baseEvent, Money payment, boolean isPositive) {
        EmployeePaymentLog paymentLog =
                EmployeePaymentLog.createPaymentLog(baseEvent.getEmployee(), MoneyService.abs(payment), isPositive);
        paymentLogService.saveEmployeePaymentLog(paymentLog);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeBaseEvent event) {
        Employee employee = event.getEmployee();
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment())
        );
    }
}
