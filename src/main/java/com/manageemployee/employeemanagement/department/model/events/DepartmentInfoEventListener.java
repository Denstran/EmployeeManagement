package com.manageemployee.employeemanagement.department.model.events;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchPaymentLogService;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.EmployeeService;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.MoneyUtil;
import com.manageemployee.employeemanagement.util.enumType.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DepartmentInfoEventListener {
    private final CompanyBranchService companyBranchService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;
    private final CompanyBranchPaymentLogService companyBranchPaymentLogService;

    private final EmployeeService employeeService;

    public DepartmentInfoEventListener(CompanyBranchService companyBranchService,
                                       DepartmentInfoPaymentLogService departmentInfoPaymentLogService,
                                       CompanyBranchPaymentLogService companyBranchPaymentLogService,
                                       EmployeeService employeeService) {
        this.companyBranchService = companyBranchService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.companyBranchPaymentLogService = companyBranchPaymentLogService;
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
        int compareResult = MoneyUtil.compareAmounts(newBudget, oldBudget);
        return compareResult == 1;
    }

    private void processPositiveBudgetChanges(DepartmentInfoBaseEvent baseEvent) {
        Money budgetChanges = MoneyUtil.subtract(baseEvent.getNewBudget(), baseEvent.getOldBudget());
        processDepartmentInfoPaymentLogCreation(baseEvent, budgetChanges, true);
        processCompanyBranchNegativeBudgetChanges(baseEvent.getCompanyBranch(), budgetChanges);
    }

    private void processNegativeBudgetChanges(DepartmentInfoBaseEvent baseEvent) {
        Money budgetChanges = MoneyUtil.subtract(baseEvent.getOldBudget(), baseEvent.getNewBudget());
        processDepartmentInfoPaymentLogCreation(baseEvent, budgetChanges, false);
        processCompanyBranchPositiveBudgetChanges(baseEvent.getCompanyBranch(), budgetChanges);
    }

    private void processCompanyBranchPositiveBudgetChanges(CompanyBranch companyBranch, Money budgetChange) {
        companyBranch.setBudget(MoneyUtil.sum(companyBranch.getBudget(), budgetChange));
        companyBranchService.updateCompanyBranch(companyBranch);
        processCompanyBranchPaymentLogCreation(companyBranch, budgetChange, true);
    }

    private void processDepartmentInfoPaymentLogCreation(DepartmentInfoBaseEvent baseEvent, Money payment,
                                                         boolean isPositive) {
        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog.createPaymentLog(baseEvent.getCompanyBranch(),
                baseEvent.getDepartment(), MoneyUtil.abs(payment), isPositive);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void processCompanyBranchNegativeBudgetChanges(CompanyBranch companyBranch, Money budgetChange) {
        companyBranch.setBudget(MoneyUtil.subtract(companyBranch.getBudget(), budgetChange));
        companyBranchService.updateCompanyBranch(companyBranch);
        processCompanyBranchPaymentLogCreation(companyBranch, budgetChange, false);
    }

    private void processCompanyBranchPaymentLogCreation(CompanyBranch companyBranch, Money budgetChange,
                                                        boolean isPositive) {
        CompanyBranchPaymentLog paymentLog =
                CompanyBranchPaymentLog.createPaymentLog(companyBranch, MoneyUtil.abs(budgetChange), isPositive);
        companyBranchPaymentLogService.saveCompanyBranchPaymentLog(paymentLog);
    }

    private Money countTotalBudgetIncome(DepartmentInfoBaseEvent baseEvent) {
        Money employeeSalaries =
                employeeService.countEmployeeSalariesByCompanyBranchAndDepartment(baseEvent.getCompanyBranch(),
                        baseEvent.getDepartment());
        return MoneyUtil.sum(employeeSalaries, baseEvent.getOldBudget());
    }
}
