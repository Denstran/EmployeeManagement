package com.manageemployee.employeemanagement.integration;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.manageemployee.employeemanagement.mail.EmailService;
import com.manageemployee.employeemanagement.mail.VacationRequest;
import com.manageemployee.employeemanagement.mail.VacationResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
@Slf4j
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    private static final String DESTINATION_ADDRESS = "test@gmail.com";
    private static final VacationRequest VACATION_REQUEST = Mockito.mock(VacationRequest.class);
    private static final VacationResponse VACATION_RESPONSE = Mockito.mock(VacationResponse.class);

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("username", "secret"))
            .withPerMethodLifecycle(true);

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
                .thenReturn(DESTINATION_ADDRESS);
    }

    static void prepareVacationResponse() {
        Mockito.when(VACATION_RESPONSE.getVacationStartDate())
                .thenReturn(LocalDate.of(2000, Month.APRIL, 1));
        Mockito.when(VACATION_RESPONSE.getVacationEndDate())
                .thenReturn(LocalDate.of(2000, Month.APRIL, 14));
    }

    @Test
    void should_send_single_simple_mail() throws MessagingException {
        emailService.sendSimpleMessage(DESTINATION_ADDRESS, "Test", "Test");
        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertEquals("Test", GreenMailUtil.getBody(message));
        assertEquals(1, message.getAllRecipients().length);
        assertEquals(DESTINATION_ADDRESS, message.getAllRecipients()[0].toString());
    }

    @Test
    void should_send_vacation_request_mail() throws MessagingException, IOException {
        emailService.sendVacationRequestMail(DESTINATION_ADDRESS, VACATION_REQUEST);
        MimeMessage message = greenMail.getReceivedMessages()[0];
        String expectedContent =
                """
                Запрос поступил от сотрудника Requester
                Отпуск планируется с 2000-04-01 по 2000-04-14, общая продолжительность: 14
                Контактные данные сотрудника: test@gmail.com
                """;
        assertEquals(expectedContent, message.getContent());
        assertEquals(1, message.getAllRecipients().length);
        assertEquals(DESTINATION_ADDRESS, message.getAllRecipients()[0].toString());
    }

    @Test
    void should_send_correct_response_mail_when_is_not_changed() throws MessagingException, IOException {
        Mockito.when(VACATION_RESPONSE.isChanged()).thenReturn(false);
        String expectedContent =
                """
                Запрос на отпуск одобрен
                Отпуск начнётся с 2000-04-01 по 2000-04-14
                """;
        emailService.sendVacationResponseMail(DESTINATION_ADDRESS, VACATION_RESPONSE);
        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertEquals(expectedContent, message.getContent());
        assertEquals(1, message.getAllRecipients().length);
        assertEquals(DESTINATION_ADDRESS, message.getAllRecipients()[0].toString());
    }

    @Test
    void should_send_correct_response_mail_when_changed() throws MessagingException, IOException {
        Mockito.when(VACATION_RESPONSE.isChanged()).thenReturn(true);
        String expectedContent =
                """
                Запрос на отпуск изменён
                Отпуск начнётся с 2000-04-01 по 2000-04-14
                """;
        emailService.sendVacationResponseMail(DESTINATION_ADDRESS, VACATION_RESPONSE);
        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertEquals(expectedContent, message.getContent());
        assertEquals(1, message.getAllRecipients().length);
        assertEquals(DESTINATION_ADDRESS, message.getAllRecipients()[0].toString());
    }
}
