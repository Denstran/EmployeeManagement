package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeHired;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.mail.EmailService;
import com.manageemployee.employeemanagement.mail.Mail;
import com.manageemployee.employeemanagement.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class EmployeeHiredEventListener {
    private final EmailService emailService;
    private final EmployeeService employeeService;
    private final EmployeePaymentLogService employeePaymentLogService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;
    private final DepartmentInfoService departmentInfoService;

    @Autowired
    public EmployeeHiredEventListener(EmailService emailService,
                                      EmployeeService employeeService,
                                      EmployeePaymentLogService employeePaymentLogService,
                                      DepartmentInfoPaymentLogService departmentInfoPaymentLogService,
                                      DepartmentInfoService departmentInfoService) {
        this.emailService = emailService;
        this.employeeService = employeeService;
        this.employeePaymentLogService = employeePaymentLogService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.departmentInfoService = departmentInfoService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmployeeHiring(EmployeeHired employeeHired) {
        log.info("EMPLOYEE EMAIL {}", employeeHired.getEmployee().getEmail());
        processSalaryChanges(employeeHired);
        sendPasswordEmail(employeeHired.getEmployee().getEmail(), employeeHired.getPassword());
        sendHiringNotificationEmail(employeeHired.getEmployee());
    }

    private void processSalaryChanges(EmployeeHired employeeHired) {
        log.info("PROCESSING SALARY CHANGES");
        log.info("Old salary: {}, New salary: {}", employeeHired.getOldSalary(), employeeHired.getNewSalary());
        createEmployeePaymentLog(employeeHired, employeeHired.getNewSalary());
        processDepartmentBudgetChanges(getDepartmentInfo(employeeHired), employeeHired.getNewSalary());
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

    private void createEmployeePaymentLog(EmployeeHired employeeHired, Money newSalary) {
        EmployeePaymentLog paymentLog =
                EmployeePaymentLog.createPaymentLog(employeeHired.getEmployee(), Money.abs(newSalary), true);
        employeePaymentLogService.saveEmployeePaymentLog(paymentLog);
    }

    private void sendPasswordEmail(String email, String password) {
        String emailText = String.format(
                """
                    Добро пожаловать в нашу компанию! Для авторизации в системе вам понадобятся данные для входа:
                    Логин: %s
                    Пароль: %s
                """, email, password);
        log.info("ACTUAL EMAIL: {}", email);
        log.info("MAIL MESSAGE: {}", emailText);
        Mail mail = Mail.prepareSimpleMail(email, "Данные для входа", emailText);
        emailService.sendEmail(mail);
    }

    private void sendHiringNotificationEmail(Employee employee) {
        Employee headOfDepartment = employeeService.getDepartmentBoss(
                employee.getCompanyBranch(),
                employee.getPosition().getDepartment());

        String text = String.format(
                """
                У вас появился новый подчинённый.
                Его контакты: %s
                """, employee.getEmployeeContacts());

        emailService.sendEmail(Mail.prepareSimpleMail(headOfDepartment.getEmail(), "Новый сотрудник", text));
    }

    private DepartmentInfo getDepartmentInfo(EmployeeHired event) {
        Employee employee = event.getEmployee();
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment())
        );
    }
}
