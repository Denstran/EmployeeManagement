package com.manageemployee.employeemanagement.task.model.event;

import com.manageemployee.employeemanagement.mail.EmailService;
import com.manageemployee.employeemanagement.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TaskEventListener {
    private final EmailService emailService;

    @Autowired
    public TaskEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskCreatedEventHandler(TaskCreated taskCreated) {
        String emailText = String.format(
                """
                Вам выдана задача от: %s
                Описание задачи: %s
                Срок сдачи задачи: %s
                """, taskCreated.getGiverContacts(), taskCreated.getTaskDescription(), taskCreated.getTaskDeadLine());
        String to = taskCreated.getEmployeeEmail();
        Mail mail = Mail.prepareSimpleMail(to, "Выдана задача", emailText);

        emailService.sendEmail(mail);
    }
}
