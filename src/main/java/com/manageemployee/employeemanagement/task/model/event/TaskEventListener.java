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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskFinishedEventHandler(TaskFinished taskFinished) {
        String emailText = String.format(
                """
                Сотрудник %s сдал задачу на проверку
                Описание задачи: %s
                Дата сдачи: %s
                """, taskFinished.getOwnerContacts(), taskFinished.getTaskDescription(), taskFinished.getFinishDate()
        );
        String to = taskFinished.getGiverEmail();
        Mail mail = Mail.prepareSimpleMail(to, "Задача на проверку", emailText);
        emailService.sendEmail(mail);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskApprovedEventHandler(TaskApproved taskApproved) {
        String emailText = String.format(
                """
                Начальник подтвердил выполнение задачу: %s
                """, taskApproved.getTaskDescription()
        );

        String to = taskApproved.getOwnerEmail();
        Mail mail = Mail.prepareSimpleMail(to, "Задача выполнена", emailText);
        emailService.sendEmail(mail);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskCanceledEventHandler(TaskCanceled taskCanceled) {
        String emailText = String.format(
                """
                Начальник отменил выполнение задачу: %s
                Контакты начальник: %s
                """, taskCanceled.getTaskDescription(), taskCanceled.getTaskGiverContacts()
        );

        String to = taskCanceled.getTaskOwnerEmail();
        Mail mail = Mail.prepareSimpleMail(to, "Задача отменена", emailText);
        emailService.sendEmail(mail);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskDisapprovedEventHandler(TaskDisapproved taskDisapproved) {
        String emailText = String.format(
                """
                Начальник не принял выполнение задачи: %s
                Контакты начальника: %s
                """, taskDisapproved.getTaskDescription(), taskDisapproved.getGiverContacts()
        );

        String to = taskDisapproved.getOwnerEmail();
        Mail mail = Mail.prepareSimpleMail(to, "Задача не выполнена", emailText);
        emailService.sendEmail(mail);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskDeadlineExtendedEventHandler(TaskDeadlineExtended taskDeadlineExtended) {
        String emailText = String.format(
                """
                Срок сдачи задачи передвинут
                Новый срок: %s
                Описание задачи: %s
                Контакты начальника: %s
                """,
                taskDeadlineExtended.getExtendedDeadline(),
                taskDeadlineExtended.getTaskDescription(),
                taskDeadlineExtended.getGiverContacts()
        );

        String to = taskDeadlineExtended.getOwnerEmail();

        Mail mail = Mail.prepareSimpleMail(to, "Срок сдачи задачи передвинут", emailText);
        emailService.sendEmail(mail);
    }
}
