package com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.event.employeeEvent.EmployeeDepartmentChanged;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.mail.EmailService;
import com.manageemployee.employeemanagement.mail.Mail;
import com.manageemployee.employeemanagement.util.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmployeeDepartmentChangedEventListener {
    private final DepartmentInfoService departmentInfoService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;
    private final EmailService emailService;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeDepartmentChangedEventListener(DepartmentInfoService departmentInfoService, DepartmentInfoPaymentLogService departmentInfoPaymentLogService, EmailService emailService, EmployeeService employeeService) {
        this.departmentInfoService = departmentInfoService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.emailService = emailService;
        this.employeeService = employeeService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEmployeeDepartmentChangedEvent(EmployeeDepartmentChanged event) {
        processBudgetChanges(event);
        sendNotificationToNewHeadOfDepartment(event);
    }

    private void sendNotificationToNewHeadOfDepartment(EmployeeDepartmentChanged event) {
        Employee headOfNewDepartment = employeeService.getDepartmentBoss(event.getNewDepartmentInfoPK());
        if (headOfNewDepartment == null) return;

        String message = String.format(
                """
                К Вам переведён новый сотрудник! Его контакты: %s
                """, event.getEmployeeContacts());
        Mail mail = Mail.prepareSimpleMail(headOfNewDepartment.getEmail(), "Новый сотрудник", message);
        emailService.sendEmail(mail);
    }

    private void processBudgetChanges(EmployeeDepartmentChanged event) {
        processOldDepartmentBudgetChanges(event);
        processNewDepartmentBudgetChanges(event);
    }

    private void processNewDepartmentBudgetChanges(EmployeeDepartmentChanged event) {
        DepartmentInfo departmentInfo = departmentInfoService.getById(event.getNewDepartmentInfoPK());
        departmentInfoService.allocateBudgetForSalary(departmentInfo, event.getEmployeeSalary());
        createPaymentLog(event.getNewDepartmentInfoPK(), event.getEmployeeSalary(), false);
    }

    private void processOldDepartmentBudgetChanges(EmployeeDepartmentChanged event) {
        DepartmentInfo departmentInfo = departmentInfoService.getById(event.getOldDepartmentInfoPK());
        departmentInfoService.employeeSalaryReducing(departmentInfo, event.getEmployeeSalary());
        createPaymentLog(event.getOldDepartmentInfoPK(), event.getEmployeeSalary(), true);
    }

    private void createPaymentLog(CompanyBranchDepartmentPK pk, Money budgetChanges, boolean isPositive) {
        DepartmentInfoPaymentLog departmentInfoPaymentLog =
                DepartmentInfoPaymentLog.createPaymentLog(pk, budgetChanges, isPositive);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(departmentInfoPaymentLog);
    }

}
