package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.*;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoRegistered;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoRemoved;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoUpdated;
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
    private final MoneyService moneyService;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentInfoEventListener(CompanyBranchService companyBranchService, PaymentLogService paymentLogService, MoneyService moneyService, EmployeeService employeeService) {
        this.companyBranchService = companyBranchService;
        this.paymentLogService = paymentLogService;
        this.moneyService = moneyService;
        this.employeeService = employeeService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void registerDepartmentInfoEventHandler(DepartmentInfoRegistered departmentInfoRegistered) {
        DepartmentInfoPaymentLog paymentLog = departmentInfoRegistered.getDepartmentInfoPaymentLog();
        CompanyBranch companyBranch = paymentLog.getCompanyBranch();
        Money departmentBudget = paymentLog.getPaymentAmount();
        companyBranch.setBudget(moneyService.subtract(companyBranch.getBudget(), departmentBudget));

        CompanyBranchPaymentLog companyBranchPaymentLog =
                CompanyBranchPaymentLog.createPaymentLog(companyBranch, departmentBudget, false);

        companyBranchService.updateCompanyBranch(companyBranch);
        paymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
        paymentLogService.saveCompanyBranchPaymentLog(companyBranchPaymentLog);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentUpdatedEventHandler(DepartmentInfoUpdated departmentInfoUpdated) {
        processBudgetChanges(departmentInfoUpdated);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void departmentRemovedEventHandler(DepartmentInfoRemoved departmentInfoRemoved) {
        CompanyBranch companyBranch = departmentInfoRemoved.getCompanyBranch();
        Money employeeSalaries =
                employeeService.countEmployeeSalariesByCompanyBranchAndDepartment(companyBranch,
                                departmentInfoRemoved.getDepartment());

        Money totalBudgetIncome = moneyService.sum(employeeSalaries, departmentInfoRemoved.getDepartmentBudget());
        companyBranch.setBudget(moneyService.sum(companyBranch.getBudget(), totalBudgetIncome));

        CompanyBranchPaymentLog paymentLog
                = CompanyBranchPaymentLog.createPaymentLog(companyBranch, totalBudgetIncome, true);

        paymentLogService.deleteEmployeesPaymentLogsByCompanyBranchAndDepartment(companyBranch,
                departmentInfoRemoved.getDepartment());
        employeeService.deleteAllByCompanyBranchAndDepartment(companyBranch, departmentInfoRemoved.getDepartment());
        paymentLogService.deleteDepartmentInfoPaymentLogs(companyBranch, departmentInfoRemoved.getDepartment());

        companyBranchService.updateCompanyBranch(companyBranch);
        paymentLogService.saveCompanyBranchPaymentLog(paymentLog);
    }

    private void processBudgetChanges(DepartmentInfoUpdated departmentInfoUpdated) {
        Money oldDepBudget = departmentInfoUpdated.getOldDepartmentBudget();
        Money newDepBudget = departmentInfoUpdated.getNewDepartmentBudget();
        if (oldDepBudget.equals(newDepBudget)) return;

        CompanyBranch companyBranch = departmentInfoUpdated.getCompanyBranch();
        Department department = departmentInfoUpdated.getDepartment();

        Money amountToReduce = moneyService.subtract(newDepBudget, oldDepBudget);

        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog.createPaymentLog(companyBranch, department,
                moneyService.abs(amountToReduce), moneyService.isPositive(amountToReduce));
        CompanyBranchPaymentLog companyBranchPaymentLog =
                CompanyBranchPaymentLog.createPaymentLog(companyBranch, moneyService.abs(amountToReduce),
                        !moneyService.isPositive(amountToReduce));

        paymentLogService.saveCompanyBranchPaymentLog(companyBranchPaymentLog);
        paymentLogService.saveDepartmentInfoPaymentLog(paymentLog);

        companyBranch.setBudget(moneyService.subtract(companyBranch.getBudget(), amountToReduce));
        companyBranchService.updateCompanyBranch(companyBranch);
    }
}
