package com.manageemployee.employeemanagement.mail;

import com.manageemployee.employeemanagement.EmployeeManagementApplication;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor
@Data
@Slf4j
public class Mail {
    private static final Properties properties;

    static {
        properties = new Properties();
        try(InputStream inputStream =
                    EmployeeManagementApplication.class.getClassLoader().getResourceAsStream("application.yml")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    private static String from = properties.getProperty("username");;
    private String to;
    private String subject;
    private String content;

    private Mail(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public static Mail prepareVacationRequestEmail(String to, VacationRequest vacationRequest) {
        validateDestinationAddress(to);
        String subject = "Запрос отпуска";
        String content =
                """
                Запрос поступил от сотрудника %s
                Отпуск планируется с %s по %s, общая продолжительность: %d
                Количество выделенных отпускных: %s
                Контактные данные сотрудника: %s
                """;

        return new Mail(to, subject, String.format(content,
                vacationRequest.getRequesterName(),
                vacationRequest.getVacationStartDate().toString(),
                vacationRequest.getVacationEndDate().toString(),
                vacationRequest.getVacationDays(),
                vacationRequest.getVacationMoney().getAmount(),
                vacationRequest.getRequesterContacts()));
    }

    public static Mail prepareVacationResponseEmail(String to, VacationResponse response) {
        validateDestinationAddress(to);
        String subject = "Ответ на запрос на отпуск";
        String content =
                """
                Запрос на отпуск %s
                Отпуск начнётся с %s по %s
                """;
        String vacationStatus = response.isChanged() ? "изменён" : "одобрен";

        return new Mail(to, subject, String.format(content,
                vacationStatus,
                response.getVacationStartDate().toString(),
                response.getVacationEndDate().toString()));
    }

    public static Mail prepareSimpleMail(String to, String subject, String content) {
        validateDestinationAddress(to);
        if (isSubjectNotValid(subject))
            throw new IllegalArgumentException("Тема письма не должна быть пустой!");
        return new Mail(to, subject, content);
    }

    private static boolean isSubjectNotValid(String subject) {
        return subject == null || subject.isEmpty();
    }

    private static void validateDestinationAddress(String destinationAddress) {
        boolean allowLocal = false;
        boolean isValid = EmailValidator.getInstance(allowLocal).isValid(destinationAddress);
        if (isValid) return;

        throw new IllegalArgumentException("Не корректный адрес получателя письма!");
    }

}
