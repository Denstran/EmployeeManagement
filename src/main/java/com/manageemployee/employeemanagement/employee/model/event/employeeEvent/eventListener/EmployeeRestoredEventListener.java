package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeRestored;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor.AddRoleProcessor;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor.RemoveRoleProcessor;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@Slf4j
public class EmployeeRestoredEventListener {

    private final DepartmentInfoService departmentInfoService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;
    private final EmployeePaymentLogService employeePaymentLogService;

    private final List<AddRoleProcessor> addRoleProcessors;
    private final List<RemoveRoleProcessor> removeRoleProcessors;

    @Autowired
    public EmployeeRestoredEventListener(DepartmentInfoService departmentInfoService,
                                         DepartmentInfoPaymentLogService departmentInfoPaymentLogService,
                                         EmployeePaymentLogService employeePaymentLogService,
                                         List<AddRoleProcessor> addRoleProcessors,
                                         List<RemoveRoleProcessor> removeRoleProcessors) {
        this.departmentInfoService = departmentInfoService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.employeePaymentLogService = employeePaymentLogService;
        this.addRoleProcessors = addRoleProcessors;
        this.removeRoleProcessors = removeRoleProcessors;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void restoreEmployeeEventHandler(EmployeeRestored employeeRestored) {
        processSalaryChanges(employeeRestored);
        unBlockAccount(employeeRestored);
        addRoleProcessors.forEach(addRoleProcessor -> addRoleProcessor.processAddRole(employeeRestored));
    }

    private void unBlockAccount(EmployeeRestored employeeRestored) {
        User user = employeeRestored.getEmployee().getUser();
        user.setEnabled(true);
    }


    private void processSalaryChanges(EmployeeRestored employeeRestored) {
        log.info("PROCESSING SALARY CHANGES");
        log.info("Old salary: {}, New salary: {}", employeeRestored.getOldSalary(), employeeRestored.getNewSalary());
        createEmployeePaymentLog(employeeRestored, employeeRestored.getNewSalary());
        processDepartmentBudgetChanges(getDepartmentInfo(employeeRestored), employeeRestored.getNewSalary());
    }

    private void processDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.allocateBudgetForSalary(departmentInfo, budgetChanges);
        createDepartmentInfoPaymentLog(departmentInfo, budgetChanges);
    }

    private void createDepartmentInfoPaymentLog(DepartmentInfo departmentInfo, Money paymentAmount) {
        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog
                .createPaymentLog(departmentInfo.getPk(), Money.abs(paymentAmount), false);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void createEmployeePaymentLog(EmployeeRestored employeeRestored, Money newSalary) {
        EmployeePaymentLog paymentLog =
                EmployeePaymentLog.createPaymentLog(employeeRestored.getEmployee(), Money.abs(newSalary), true);
        employeePaymentLogService.saveEmployeePaymentLog(paymentLog);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeRestored event) {
        Employee employee = event.getEmployee();
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment())
        );
    }
}
