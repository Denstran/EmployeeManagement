package com.manageemployee.employeemanagement.employee.event.vacationEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.mail.EmailService;
import com.manageemployee.employeemanagement.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class VacationEventListener {
    private final EmailService emailService;
    private final EmployeeService employeeService;

    @Autowired
    public VacationEventListener(EmailService emailService, EmployeeService employeeService) {
        this.emailService = emailService;
        this.employeeService = employeeService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void vacationRequestCreatedEventListener(VacationRequestCreated event) {
        Employee employee = employeeService.getDepartmentBoss(event.getCompanyBranch(), event.getDepartment());
        emailService.sendVacationRequestMail(employee.getEmail(), event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void vacationRequestUpdatedEventListener(VacationRequestUpdated event) {
        sendVacationResponse(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void vacationRequestApprovedEventListener(VacationRequestApproved event) {
        sendVacationResponse(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void vacationRequestCancelledEventListener(VacationRequestCancelled event) {
        Mail mail = Mail.prepareSimpleMail(event.getHeadOfDepartment().getEmail(),
                "Ответ на запрос на отпуск",
                String.format("Сотрудник %s отменил запрос на отпуск", event.getVacationOwner().getName()));
        emailService.sendEmail(mail);
    }

    private void sendVacationResponse(AbstractVacationResponse vacationResponse) {
        Employee employee = vacationResponse.getEmployee();
        Employee employeeBoss = employeeService.getDepartmentBoss(employee.getCompanyBranch(),
                employee.getPosition().getDepartment());

        emailService.sendVacationResponseMail(employee.getEmail(), vacationResponse);
        emailService.sendVacationResponseMail(employeeBoss.getEmail(), vacationResponse);
    }
}
