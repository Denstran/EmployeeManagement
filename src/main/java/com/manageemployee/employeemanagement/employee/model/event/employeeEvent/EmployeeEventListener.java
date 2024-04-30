package com.manageemployee.employeemanagement.employee.model.event.employeeEvent;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.employee.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.enumType.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Set;

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
        processPositionChanges(employeeUpdated);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void fireEmployeeEventHandler(EmployeeFired employeeFired) {
        processSalaryChanges(employeeFired, Action.DELETE);
        blockAccount(employeeFired);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void restoreEmployeeEventHandler(EmployeeRestored employeeRestored) {
        processSalaryChanges(employeeRestored, Action.CREATE);
        unBlockAccount(employeeRestored);
        restoreRoles(employeeRestored);
    }

    private void restoreRoles(EmployeeRestored employeeRestored) {
        User user = employeeRestored.getEmployee().getUser();
        Set<UserRole> roles = prepareRoles(employeeRestored);
        user.setRoles(roles);
    }

    private void unBlockAccount(EmployeeRestored employeeRestored) {
        User user = employeeRestored.getEmployee().getUser();
        user.setEnabled(true);
    }

    private Set<UserRole> prepareRoles(EmployeeRestored employeeRestored) {
        Set<UserRole> roles = new HashSet<>();
        if (isLeadingPosition(employeeRestored)) roles.add(UserRole.ROLE_HEAD_OF_DEPARTMENT);
        if (isHR(employeeRestored)) roles.add(UserRole.ROLE_HR);
        else roles.add(UserRole.ROLE_EMPLOYEE);

        return roles;
    }

    private boolean isHR(EmployeeRestored employeeRestored) {
        Employee employee = employeeRestored.getEmployee();
        return DepartmentType.HR.equals(employee.getPosition().getDepartment().getDepartmentType());
    }

    private boolean isLeadingPosition(EmployeeRestored employeeRestored) {
        Employee employee = employeeRestored.getEmployee();
        return employee.getPosition().isLeading();
    }

    private void blockAccount(EmployeeFired employeeFired) {
        User user = employeeFired.getEmployee().getUser();
        user.setEnabled(false);
        user.clearRoles();
    }

    private void processPositionChanges(EmployeeUpdated employeeUpdated) {
        if (isSamePosition(employeeUpdated)) return;
        if (isPositionChangedToLeading(employeeUpdated)) addLeadingRole(employeeUpdated);
        else removeLeadingRole(employeeUpdated);
    }

    private boolean isPositionChangedToLeading(EmployeeUpdated employeeUpdated) {
        return employeeUpdated.getEmployee().getPosition().isLeading();
    }

    private void addLeadingRole(EmployeeUpdated employeeUpdated) {
        User user = employeeUpdated.getEmployee().getUser();
        user.addRole(UserRole.ROLE_HEAD_OF_BRANCH);
    }

    private void removeLeadingRole(EmployeeUpdated employeeUpdated) {
        User user = employeeUpdated.getEmployee().getUser();
        user.removeRole(UserRole.ROLE_HEAD_OF_DEPARTMENT);
    }

    private boolean isSamePosition(EmployeeUpdated employeeUpdated) {
        return employeeUpdated.getEmployee().getPosition().equals(employeeUpdated.getOldPosition());
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
        Money salaryChanges = Money.subtract(baseEvent.getOldSalary(), baseEvent.getNewSalary());
        processEmployeePaymentLogCreation(baseEvent, salaryChanges, false);
        processPositiveDepartmentBudgetChanges(getDepartmentInfo(baseEvent), salaryChanges);
    }

    private void processPositiveSalaryChanges(EmployeeBaseEvent baseEvent) {
        Money salaryChanges = Money.subtract(baseEvent.getNewSalary(), baseEvent.getOldSalary());
        processEmployeePaymentLogCreation(baseEvent, salaryChanges, true);
        processNegativeDepartmentBudgetChanges(getDepartmentInfo(baseEvent), salaryChanges);
    }

    private void processNegativeDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.allocateBudgetForSalary(departmentInfo, budgetChanges);
        processDepartmentInfoPaymentLogCreation(departmentInfo, Money.abs(budgetChanges), false);
    }

    private boolean isPositiveSalaryChanges(Money newSalary, Money oldSalary) {
        int compareResult = Money.compareTo(newSalary, oldSalary);
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
                .createPaymentLog(departmentInfo.getPk(), Money.abs(paymentAmount), isPositive);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void processEmployeePaymentLogCreation(EmployeeBaseEvent baseEvent, Money payment, boolean isPositive) {
        EmployeePaymentLog paymentLog =
                EmployeePaymentLog.createPaymentLog(baseEvent.getEmployee(), Money.abs(payment), isPositive);
        employeePaymentLogService.saveEmployeePaymentLog(paymentLog);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeBaseEvent event) {
        Employee employee = event.getEmployee();
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment())
        );
    }
}