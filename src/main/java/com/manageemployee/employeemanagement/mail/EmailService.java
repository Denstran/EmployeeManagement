package com.manageemployee.employeemanagement.mail;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Getter
@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(Mail mail) {
        log.info("FROM EMAIL_SERVICE: SENDING MAIL TO {}, FROM {}, SUBJECT: {}, TEXT {}",
                mail.getTo(), Mail.getFrom(), mail.getSubject(), mail.getContent());
        SimpleMailMessage message = mapMailToSimpleMailMessage(mail);
        log.info("FROM EMPLOYEE_SERVICE: MESSAGE PREPARED: {}", message);
        mailSender.send(message);
    }

    public void sendVacationRequestMail(String to, VacationRequest vacationRequest) {
        Mail mail = Mail.prepareVacationRequestEmail(to, vacationRequest);
        sendEmail(mail);
    }

    public void sendVacationResponseMail(String to, VacationResponse vacationResponse) {
        Mail mail = Mail.prepareVacationResponseEmail(to, vacationResponse);
        sendEmail(mail);
    }

    public void sendSimpleMessage(String to, String subject, String content) {
        Mail mail = Mail.prepareSimpleMail(to, subject, content);
        sendEmail(mail);
    }

    private SimpleMailMessage mapMailToSimpleMailMessage(Mail mail) {
        log.info("MAPPING Mail to SimpleMailMessage: Mail object: {}", mail);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Mail.getFrom());
        message.setTo(mail.getTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        log.info("FINISHED MAPPING Mail to SimpleMailMessage: SimpleMailMessage object {}", message);
        return message;
    }
}
