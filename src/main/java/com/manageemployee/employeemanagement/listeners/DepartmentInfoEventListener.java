package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.*;
import com.manageemployee.employeemanagement.model.events.departmentInfoEvents.DepartmentInfoBaseEvent;
import com.manageemployee.employeemanagement.model.events.departmentInfoEvents.DepartmentInfoRegistered;
import com.manageemployee.employeemanagement.model.events.departmentInfoEvents.DepartmentInfoRemoved;
import com.manageemployee.employeemanagement.model.events.departmentInfoEvents.DepartmentInfoUpdated;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DepartmentInfoEventListener {
    private final CompanyBranchService companyBranchService;
    private final PaymentLogService paymentLogService;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentInfoEventListener(CompanyBranchService companyBranchService, PaymentLogService paymentLogService,
                                       EmployeeService employeeService) {
        this.companyBranchService = companyBranchService;
        this.paymentLogService = paymentLogService;
        this.employeeService = employeeService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerDepartmentInfoEventHandler(DepartmentInfoRegistered departmentInfoRegistered) {
        processBudgetChanges(departmentInfoRegistered, Action.CREATE);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentUpdatedEventHandler(DepartmentInfoUpdated departmentInfoUpdated) {
        processBudgetChanges(departmentInfoUpdated, Action.UPDATE);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentRemovedEventHandler(DepartmentInfoRemoved departmentInfoRemoved) {
        processBudgetChanges(departmentInfoRemoved, Action.DELETE);
    }

    private void processBudgetChanges(DepartmentInfoBaseEvent baseEvent, Action action) {
        switch (action) {
            case CREATE -> processCompanyBranchNegativeBudgetChanges(baseEvent.getCompanyBranch(),
                    baseEvent.getNewBudget());
            case UPDATE -> processDepInfoChanges(baseEvent);
            case DELETE -> processDepartmentInfoRemoval(baseEvent);
        }
    }

    private void processDepartmentInfoRemoval(DepartmentInfoBaseEvent baseEvent) {
        CompanyBranch companyBranch = baseEvent.getCompanyBranch();
        Department department = baseEvent.getDepartment();
        Money totalBudgetIncome = countTotalBudgetIncome(baseEvent);
        processCompanyBranchPositiveBudgetChanges(companyBranch, totalBudgetIncome);
        employeeService.deleteAllByCompanyBranchAndDepartment(companyBranch, department);
    }

    private void processDepInfoChanges(DepartmentInfoBaseEvent baseEvent) {
        Money oldBudget = baseEvent.getOldBudget();
        Money newBudget = baseEvent.getNewBudget();
        if (oldBudget.equals(newBudget)) return;

        if (isPositiveBudgetChanges(newBudget, oldBudget)) processPositiveBudgetChanges(baseEvent);
        else processNegativeBudgetChanges(baseEvent);
    }

    private boolean isPositiveBudgetChanges(Money newBudget, Money oldBudget) {
        int compareResult = MoneyService.compareAmounts(newBudget, oldBudget);
        return compareResult == 1;
    }

    private void processPositiveBudgetChanges(DepartmentInfoBaseEvent baseEvent) {
        Money budgetChanges = MoneyService.subtract(baseEvent.getNewBudget(), baseEvent.getOldBudget());
        processDepartmentInfoPaymentLogCreation(baseEvent, budgetChanges, true);
        processCompanyBranchNegativeBudgetChanges(baseEvent.getCompanyBranch(), budgetChanges);
    }

    private void processNegativeBudgetChanges(DepartmentInfoBaseEvent baseEvent) {
        Money budgetChanges = MoneyService.subtract(baseEvent.getOldBudget(), baseEvent.getNewBudget());
        processDepartmentInfoPaymentLogCreation(baseEvent, budgetChanges, false);
        processCompanyBranchPositiveBudgetChanges(baseEvent.getCompanyBranch(), budgetChanges);
    }

    private void processCompanyBranchPositiveBudgetChanges(CompanyBranch companyBranch, Money budgetChange) {
        companyBranch.setBudget(MoneyService.sum(companyBranch.getBudget(), budgetChange));
        companyBranchService.updateCompanyBranch(companyBranch);
        processCompanyBranchPaymentLogCreation(companyBranch, budgetChange, true);
    }

    private void processDepartmentInfoPaymentLogCreation(DepartmentInfoBaseEvent baseEvent, Money payment,
                                                         boolean isPositive) {
        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog.createPaymentLog(baseEvent.getCompanyBranch(),
                baseEvent.getDepartment(), MoneyService.abs(payment), isPositive);
        paymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void processCompanyBranchNegativeBudgetChanges(CompanyBranch companyBranch, Money budgetChange) {
        companyBranch.setBudget(MoneyService.subtract(companyBranch.getBudget(), budgetChange));
        companyBranchService.updateCompanyBranch(companyBranch);
        processCompanyBranchPaymentLogCreation(companyBranch, budgetChange, false);
    }

    private void processCompanyBranchPaymentLogCreation(CompanyBranch companyBranch, Money budgetChange,
                                                        boolean isPositive) {
        CompanyBranchPaymentLog paymentLog =
                CompanyBranchPaymentLog.createPaymentLog(companyBranch, MoneyService.abs(budgetChange), isPositive);
        paymentLogService.saveCompanyBranchPaymentLog(paymentLog);
    }

    private Money countTotalBudgetIncome(DepartmentInfoBaseEvent baseEvent) {
        Money employeeSalaries =
                employeeService.countEmployeeSalariesByCompanyBranchAndDepartment(baseEvent.getCompanyBranch(),
                        baseEvent.getDepartment());
        return MoneyService.sum(employeeSalaries, baseEvent.getOldBudget());
    }
}
