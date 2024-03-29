package com.manageemployee.employeemanagement.employee.model.event;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.employee.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.MoneyUtil;
import com.manageemployee.employeemanagement.util.enumType.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmployeeEventListener {
    private final DepartmentInfoService departmentInfoService;
    private final EmployeePaymentLogService employeePaymentLogService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;

    @Autowired
    public EmployeeEventListener(DepartmentInfoService departmentInfoService,
                                 EmployeePaymentLogService employeePaymentLogService,
                                 DepartmentInfoPaymentLogService departmentInfoPaymentLogService) {
        this.departmentInfoService = departmentInfoService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.employeePaymentLogService = employeePaymentLogService;
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
        Money salaryChanges = MoneyUtil.subtract(baseEvent.getOldSalary(), baseEvent.getNewSalary());
        processEmployeePaymentLogCreation(baseEvent, salaryChanges, false);
        processPositiveDepartmentBudgetChanges(getDepartmentInfo(baseEvent), salaryChanges);
    }

    private void processPositiveSalaryChanges(EmployeeBaseEvent baseEvent) {
        Money salaryChanges = MoneyUtil.subtract(baseEvent.getNewSalary(), baseEvent.getOldSalary());
        processEmployeePaymentLogCreation(baseEvent, salaryChanges, true);
        processNegativeDepartmentBudgetChanges(getDepartmentInfo(baseEvent), salaryChanges);
    }

    private void processNegativeDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.allocateBudgetForSalary(departmentInfo, budgetChanges);
        processDepartmentInfoPaymentLogCreation(departmentInfo, MoneyUtil.abs(budgetChanges), false);
    }

    private boolean isPositiveSalaryChanges(Money newSalary, Money oldSalary) {
        int compareResult = MoneyUtil.compareAmounts(newSalary, oldSalary);
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
                .createPaymentLog(departmentInfo.getPk(), MoneyUtil.abs(paymentAmount), isPositive);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void processEmployeePaymentLogCreation(EmployeeBaseEvent baseEvent, Money payment, boolean isPositive) {
        EmployeePaymentLog paymentLog =
                EmployeePaymentLog.createPaymentLog(baseEvent.getEmployee(), MoneyUtil.abs(payment), isPositive);
        employeePaymentLogService.saveEmployeePaymentLog(paymentLog);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeBaseEvent event) {
        Employee employee = event.getEmployee();
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment())
        );
    }
}
