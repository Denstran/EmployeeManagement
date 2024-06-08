package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeUpdated;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor.AddRoleProcessor;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener.roleProcessor.RemoveRoleProcessor;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.mail.EmailService;
import com.manageemployee.employeemanagement.mail.Mail;
import com.manageemployee.employeemanagement.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@Slf4j
public class EmployeeUpdatedEventHandler {
    private final EmployeePaymentLogService employeePaymentLogService;
    private final DepartmentInfoService departmentInfoService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;
    private final EmailService emailService;

    private final List<AddRoleProcessor> addRoleProcessors;
    private final List<RemoveRoleProcessor> removeRoleProcessors;

    @Autowired
    public EmployeeUpdatedEventHandler(EmployeePaymentLogService employeePaymentLogService,
                                       DepartmentInfoService departmentInfoService,
                                       DepartmentInfoPaymentLogService departmentInfoPaymentLogService,
                                       EmailService emailService,
                                       List<AddRoleProcessor> addRoleProcessors,
                                       List<RemoveRoleProcessor> removeRoleProcessors) {
        this.employeePaymentLogService = employeePaymentLogService;
        this.departmentInfoService = departmentInfoService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.emailService = emailService;
        this.addRoleProcessors = addRoleProcessors;
        this.removeRoleProcessors = removeRoleProcessors;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEmployeeUpdating(EmployeeUpdated employeeUpdated) {
        log.info("UPDATING EMPLOYEE");
        log.info("Old salary: {}, New salary: {}", employeeUpdated.getOldSalary(), employeeUpdated.getNewSalary());

        processSalaryChanges(employeeUpdated);
        processPositionChanges(employeeUpdated);
    }

    private void processPositionChanges(EmployeeUpdated employeeUpdated) {
        log.info("PROCESSING POSITION CHANGES");
        log.info("Old position: {}, New position {}",
                employeeUpdated.getOldPosition().getPositionName(), employeeUpdated.getEmployee().getPosition());
        if (isSamePosition(employeeUpdated)) return;

        addRoleProcessors.forEach(addRoleProcessor -> addRoleProcessor.processAddRole(employeeUpdated));
        removeRoleProcessors.forEach(removeRoleProcessor -> removeRoleProcessor.processRolesRemoval(employeeUpdated));

        String text = String.format(
                """
                Ваша должность была изменена, вот ваши текущая должность: %s
                """, employeeUpdated.getEmployee().getPosition().getPositionName());
        String to = employeeUpdated.getEmployee().getEmail();
        Mail mail = Mail.prepareSimpleMail(to, "Изменение должности", text);
        emailService.sendEmail(mail);
    }

    private boolean isSamePosition(EmployeeUpdated employeeUpdated) {
        return employeeUpdated.getEmployee().getPosition().getId().equals(employeeUpdated.getOldPosition().getId());
    }

    private void processSalaryChanges(EmployeeUpdated employeeUpdated) {
        Money oldSalary = employeeUpdated.getOldSalary();
        Money newSalary = employeeUpdated.getNewSalary();
        log.info("OLD SALARY: {}, NEW SALARY: {}, EQUALITY RESULT: {}",
                oldSalary, newSalary, oldSalary.equals(newSalary));

        if (oldSalary.equals(newSalary)) return;

        if (isPositiveSalaryChanges(newSalary, oldSalary))
            processPositiveSalaryChanges(employeeUpdated);
        else
            processNegativeSalaryChanges(employeeUpdated);
    }

    private boolean isPositiveSalaryChanges(Money newSalary, Money oldSalary) {
        int compareResult = Money.compareTo(newSalary, oldSalary);
        return compareResult == 1;
    }

    private void processPositiveSalaryChanges(EmployeeUpdated employeeUpdated) {
        log.info("PROCESSING POSITIVE SALARY CHANGES");
        Money salaryChanges = Money.subtract(employeeUpdated.getNewSalary(), employeeUpdated.getOldSalary());
        createEmployeePaymentLog(employeeUpdated, salaryChanges, true);
        processNegativeDepartmentBudgetChanges(getDepartmentInfo(employeeUpdated), salaryChanges);
    }

    private void processNegativeSalaryChanges(EmployeeUpdated employeeUpdated) {
        log.info("PROCESSING NEGATIVE SALARY CHANGES");
        Money salaryChanges = Money.subtract(employeeUpdated.getOldSalary(), employeeUpdated.getNewSalary());
        createEmployeePaymentLog(employeeUpdated, salaryChanges, false);
        processPositiveDepartmentBudgetChanges(getDepartmentInfo(employeeUpdated), salaryChanges);
    }

    private void processPositiveDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.employeeSalaryReducing(departmentInfo, budgetChanges);
        createDepartmentInfoPaymentLog(departmentInfo, budgetChanges, true);
    }

    private void createEmployeePaymentLog(EmployeeUpdated employeeUpdated, Money newSalary, boolean isPositive) {
        EmployeePaymentLog paymentLog =
                EmployeePaymentLog.createPaymentLog(employeeUpdated.getEmployee(), Money.abs(newSalary), isPositive);
        employeePaymentLogService.saveEmployeePaymentLog(paymentLog);
    }

    private void processNegativeDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.allocateBudgetForSalary(departmentInfo, budgetChanges);
        createDepartmentInfoPaymentLog(departmentInfo, Money.abs(budgetChanges), false);
    }

    private void createDepartmentInfoPaymentLog(DepartmentInfo departmentInfo, Money paymentAmount,
                                                boolean isPositive) {
        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog
                .createPaymentLog(departmentInfo.getPk(), Money.abs(paymentAmount), isPositive);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeUpdated event) {
        Employee employee = event.getEmployee();
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment())
        );
    }
}
