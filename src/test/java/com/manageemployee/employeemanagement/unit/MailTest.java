package com.manageemployee.employeemanagement.unit;

import com.manageemployee.employeemanagement.mail.Mail;
import com.manageemployee.employeemanagement.mail.VacationRequest;
import com.manageemployee.employeemanagement.mail.VacationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class MailTest {
    static private final VacationRequest VACATION_REQUEST = Mockito.mock(VacationRequest.class);
    static private final VacationResponse VACATION_RESPONSE = Mockito.mock(VacationResponse.class);

    @BeforeAll
    static void setUp() {
        prepareVacationRequest();
        prepareVacationResponse();
    }

    static void prepareVacationRequest() {
        Mockito.when(VACATION_REQUEST.getVacationStartDate())
                .thenReturn(LocalDate.of(2000, Month.APRIL, 1));
        Mockito.when(VACATION_REQUEST.getVacationEndDate())
                .thenReturn(LocalDate.of(2000, Month.APRIL, 14));
        Mockito.when(VACATION_REQUEST.getVacationDays())
                .thenReturn(14L);
        Mockito.when(VACATION_REQUEST.getRequesterName())
                .thenReturn("Requester");
        Mockito.when(VACATION_REQUEST.getRequesterContacts())
                .thenReturn("test@gmail.com");
    }

    static void prepareVacationResponse() {
        Mockito.when(VACATION_RESPONSE.getVacationStartDate())
                .thenReturn(LocalDate.of(2000, Month.APRIL, 1));
        Mockito.when(VACATION_RESPONSE.getVacationEndDate())
                .thenReturn(LocalDate.of(2000, Month.APRIL, 14));
    }

    @Test
    void should_prepare_correct_vacation_request_email() {
        Mail mail = Mail.prepareVacationRequestEmail("test@gmail.com", VACATION_REQUEST);
        String expectedContent =
                """
                Запрос поступил от сотрудника Requester
                Отпуск планируется с 2000-04-01 по 2000-04-14, общая продолжительность: 14
                Контактные данные сотрудника: test@gmail.com
                """;
        assertEquals(mail.getContent(), expectedContent);
        assertEquals("username", Mail.getFrom());
        assertEquals("test@gmail.com", mail.getTo());
        assertEquals("Запрос отпуска", mail.getSubject());
    }

    @Test
    void should_prepare_correct_vacation_response_email_when_request_is_not_changed() {
        Mockito.when(VACATION_RESPONSE.isChanged())
                .thenReturn(false);
        Mail mail = Mail.prepareVacationResponseEmail("test@mail.ru", VACATION_RESPONSE);
        assertTrue(mail.getContent().contains("одобрен"));
    }

    @Test
    void should_prepare_correct_vacation_response_email_when_request_is_changed() {
        Mockito.when(VACATION_RESPONSE.isChanged())
                .thenReturn(true);
        Mail mail = Mail.prepareVacationResponseEmail("test@mail.ru", VACATION_RESPONSE);
        assertTrue(mail.getContent().contains("изменён"));
    }

    @Test
    void should_prepare_correct_email() {
        Mail mail = Mail.prepareSimpleMail("test@mail.ru", "Test", "Test");
        assertEquals("Test", mail.getSubject());
        assertEquals("Test", mail.getContent());
        assertEquals("test@mail.ru", mail.getTo());
    }

    @Test
    void should_throw_exception_when_subject_is_empty() {
       assertThrows(IllegalArgumentException.class, () -> {
            Mail mail = Mail.prepareSimpleMail("test@mail.ru", "", "Test");
        });
    }

    @Test
    void should_throw_exception_when_subject_is_null() {
        assertThrows(IllegalArgumentException.class, () -> {
            Mail mail = Mail.prepareSimpleMail("test@mail.ru", null, "Test");
        });
    }

    @Test
    void assert_that_exception_message_is_correct_when_invalid_subject() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Mail mail = Mail.prepareSimpleMail("test@mail.ru", null, "Test");
        });
        String expectedMessage = "Тема письма не должна быть пустой!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void should_throw_exception_if_email_is_not_valid() {
        assertThrows(IllegalArgumentException.class, () -> {
            Mail mail = Mail.prepareSimpleMail("testmail.ru", "Test", "Test");
        });
    }

    @Test
    void assert_that_exception_message_is_correct_when_invalid_email_address() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Mail mail = Mail.prepareSimpleMail("testmail.ru", null, "Test");
        });
        String expectedMessage = "Не корректный адрес получателя письма!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
