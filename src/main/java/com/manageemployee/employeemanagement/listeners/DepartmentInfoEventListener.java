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

import java.util.Optional;

@Component
public class DepartmentInfoEventListener {
    private final CompanyBranchService companyBranchService;
    private final PaymentLogService paymentLogService;
    private final MoneyService moneyService;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentInfoEventListener(CompanyBranchService companyBranchService, PaymentLogService paymentLogService,
                                       MoneyService moneyService, EmployeeService employeeService) {
        this.companyBranchService = companyBranchService;
        this.paymentLogService = paymentLogService;
        this.moneyService = moneyService;
        this.employeeService = employeeService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void registerDepartmentInfoEventHandler(DepartmentInfoRegistered departmentInfoRegistered) {
        processCompanyBranchDecreasedBudget(departmentInfoRegistered);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentUpdatedEventHandler(DepartmentInfoUpdated departmentInfoUpdated) {
        processCompanyBranchBudgetChanges(departmentInfoUpdated);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentRemovedEventHandler(DepartmentInfoRemoved departmentInfoRemoved) {
        processDepartmentRemoval(departmentInfoRemoved);
    }

    private void processDepartmentRemoval(DepartmentInfoBaseEvent departmentEvent) {
        CompanyBranch companyBranch = departmentEvent.getCompanyBranch();
        Money totalBudgetIncome = countTotalBudgetIncome(departmentEvent);

        companyBranch.setBudget(moneyService.sum(companyBranch.getBudget(), totalBudgetIncome));
        updateCompanyBranchAndLogs(companyBranch, Optional.empty(), totalBudgetIncome);
        clearData(companyBranch, departmentEvent.getDepartment());
    }

    private Money countTotalBudgetIncome(DepartmentInfoBaseEvent baseEvent) {
        Money employeeSalaries =
                employeeService.countEmployeeSalariesByCompanyBranchAndDepartment(baseEvent.getCompanyBranch(),
                        baseEvent.getDepartment());
        return moneyService.sum(employeeSalaries, baseEvent.getOldBudget());
    }

    private void processCompanyBranchDecreasedBudget(DepartmentInfoBaseEvent departmentEvent) {
        CompanyBranch companyBranch = departmentEvent.getCompanyBranch();
        companyBranch.setBudget(moneyService.subtract(companyBranch.getBudget(), departmentEvent.getNewBudget()));
        updateCompanyBranchAndLogs(companyBranch, Optional.of(departmentEvent.getDepartment()), departmentEvent.getNewBudget());
    }

    private void processCompanyBranchBudgetChanges(DepartmentInfoBaseEvent departmentEvent) {
        if (departmentEvent.getNewBudget().equals(departmentEvent.getOldBudget())) return;

        CompanyBranch companyBranch = departmentEvent.getCompanyBranch();
        Money departmentBudgetChanges =
                moneyService.subtract(departmentEvent.getNewBudget(), departmentEvent.getOldBudget());

        companyBranch.setBudget(moneyService.subtract(companyBranch.getBudget(), departmentBudgetChanges));
        updateCompanyBranchAndLogs(companyBranch, Optional.of(departmentEvent.getDepartment()), departmentBudgetChanges);
    }

    private void handlePaymentLogs(CompanyBranch companyBranch, Optional<Department> department, Money budgetChange) {
        boolean isPositiveChanges = moneyService.isPositive(budgetChange);
        DepartmentInfoPaymentLog departmentInfoPaymentLog = null;

        if (department.isPresent()) {
            departmentInfoPaymentLog = DepartmentInfoPaymentLog
                    .createPaymentLog(companyBranch, department.get(), moneyService.abs(budgetChange), isPositiveChanges);
        }

        CompanyBranchPaymentLog companyBranchPaymentLog = CompanyBranchPaymentLog
                    .createPaymentLog(companyBranch, moneyService.abs(budgetChange), !isPositiveChanges);

        savePaymentLogs(companyBranchPaymentLog, Optional.ofNullable(departmentInfoPaymentLog));
    }

    private void updateCompanyBranchAndLogs(CompanyBranch companyBranch, Optional<Department> department, Money budgetChange) {
        handlePaymentLogs(companyBranch, department, budgetChange);
        companyBranchService.updateCompanyBranch(companyBranch);
    }

    private void clearData(CompanyBranch companyBranch, Department department) {
        paymentLogService.deleteEmployeesPaymentLogsByCompanyBranchAndDepartment(companyBranch, department);
        employeeService.deleteAllByCompanyBranchAndDepartment(companyBranch, department);
        paymentLogService.deleteDepartmentInfoPaymentLogs(companyBranch, department);
    }

    private void savePaymentLogs(CompanyBranchPaymentLog companyBranchPaymentLog,
                                 Optional<DepartmentInfoPaymentLog> departmentInfoPaymentLog) {
        paymentLogService.saveCompanyBranchPaymentLog(companyBranchPaymentLog);
        departmentInfoPaymentLog.ifPresent(paymentLogService::saveDepartmentInfoPaymentLog);
    }
}
