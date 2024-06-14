package com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener;

import com.manageemployee.employeemanagement.employee.event.employeeEvent.EmployeePositionChanged;
import com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor.AddRoleProcessor;
import com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener.roleProcessor.RemoveRoleProcessor;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.mail.EmailService;
import com.manageemployee.employeemanagement.mail.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@Slf4j
public class EmployeePositionChangedEventListener {
    private final EmailService emailService;
    private final EmployeeService employeeService;
    private final List<AddRoleProcessor> addRoleProcessors;
    private final List<RemoveRoleProcessor> removeRoleProcessors;

    @Autowired
    public EmployeePositionChangedEventListener(EmailService emailService,
                                                EmployeeService employeeService,
                                                List<AddRoleProcessor> addRoleProcessors,
                                                List<RemoveRoleProcessor> removeRoleProcessors) {
        this.emailService = emailService;
        this.employeeService = employeeService;
        this.addRoleProcessors = addRoleProcessors;
        this.removeRoleProcessors = removeRoleProcessors;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEmployeePositionChangedEvent(EmployeePositionChanged event) {
        log.info("Handling position changed event");
        addRoleProcessors.forEach(addRoleProcessor -> addRoleProcessor.processAddRole(event));
        removeRoleProcessors.forEach(removeRoleProcessor -> removeRoleProcessor.processRolesRemoval(event));

        sendPositionChangedNotificationToEmployee(event);
        sendNotificationToOldHeadOfDepartment(event);
    }

    private void sendNotificationToOldHeadOfDepartment(EmployeePositionChanged event) {
        String email = employeeService.getDepartmentBoss(
                event.getCompanyBranch(),
                event.getOldPosition().getDepartment()).getEmail();

        String text = String.format(
                """
                Должность вашего подчинённого была изменена на: %s
                """, event.getNewPosition().getPositionName()
        );

        Mail mail = Mail.prepareSimpleMail(email, "Изменение должности", text);
        emailService.sendEmail(mail);
    }

    private void sendPositionChangedNotificationToEmployee(EmployeePositionChanged event) {
        String text = String.format(
                """
                Ваша должность была изменена, вот ваши текущая должность: %s
                """, event.getNewPosition().getPositionName());
        String to = event.getEmail();
        Mail mail = Mail.prepareSimpleMail(to, "Изменение должности", text);
        emailService.sendEmail(mail);
    }
}
