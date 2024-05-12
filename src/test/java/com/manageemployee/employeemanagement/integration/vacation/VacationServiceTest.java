package com.manageemployee.employeemanagement.integration.vacation;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.RequestStatus;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.service.VacationService;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class VacationServiceTest {
    @Autowired
    private VacationService vacationService;
    @Autowired
    private EmployeeService employeeService;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("username", "secret"))
            .withPerMethodLifecycle(true);

    @Test
    void should_send_email_to_department_boss_when_requesting_vacation() throws MessagingException, IOException {
        Employee employee = employeeService.getById(3L);
        Employee departmentBoss = employeeService.getDepartmentBoss(employee.getCompanyBranch(),
                employee.getPosition().getDepartment());

        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(14);
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setEmployee(employee);
        vacationRequest.setVacationStartDate(currentDate);
        vacationRequest.setVacationEndDate(futureDate);

        vacationService.createRequest(vacationRequest);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        MimeMessage[] messages = greenMail.getReceivedMessages();
        MimeMessage message = messages[0];
        String actualFrom = message.getFrom()[0].toString();
        String actualTo = message.getAllRecipients()[0].toString();
        String actualContent = message.getContent().toString();
        String expectedContent =
                """
                Запрос поступил от сотрудника %s
                Отпуск планируется с %s по %s, общая продолжительность: %s
                Контактные данные сотрудника: %s
                """;

        assertEquals(1, messages.length);
        assertEquals("username", actualFrom);
        assertEquals(departmentBoss.getEmail(), actualTo);
        assertEquals(actualContent, String.format(expectedContent,
                employee.getName(),
                currentDate,
                futureDate,
                currentDate.until(futureDate, ChronoUnit.DAYS),
                employee.getPhoneNumber() + "\n" + employee.getEmail() + "\n"));
    }

    @Test
    void should_send_email_to_requester_and_boss_after_updating() throws MessagingException, IOException {
        Employee employee = employeeService.getById(3L);
        Employee departmentBoss = employeeService.getDepartmentBoss(employee.getCompanyBranch(),
                employee.getPosition().getDepartment());

        VacationRequest vacationRequest = setupVacation(employee);
        vacationRequest = vacationService.saveRequest(vacationRequest);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        vacationRequest = vacationService.getVacationById(vacationRequest.getId());
        vacationRequest.setVacationEndDate(LocalDate.now().plusDays(16));
        vacationService.updateVacation(vacationRequest);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        MimeMessage[] messages = greenMail.getReceivedMessages();
        List<String> receivers = new ArrayList<>();
        for (MimeMessage message : messages) {
            receivers.addAll(Arrays.stream(message.getAllRecipients()).map(Address::toString).toList());
        }

        assertEquals(2, messages.length);
        assertTrue(messages[0].getContent().toString().contains("изменён"));
        assertTrue(messages[1].getContent().toString().contains("изменён"));
        assertTrue(receivers.contains(employee.getEmail()));
        assertTrue(receivers.contains(departmentBoss.getEmail()));
    }

    @Test
    void should_sent_email_to_requester_and_boss_after_approving() throws MessagingException, IOException {
        Employee employee = employeeService.getById(3L);
        Employee departmentBoss = employeeService.getDepartmentBoss(employee.getCompanyBranch(),
                employee.getPosition().getDepartment());

        VacationRequest vacationRequest = setupVacation(employee);
        vacationRequest = vacationService.saveRequest(vacationRequest);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        vacationRequest = vacationService.getVacationById(vacationRequest.getId());
        vacationService.approveVacation(vacationRequest);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        MimeMessage[] messages = greenMail.getReceivedMessages();
        List<String> receivers = new ArrayList<>();
        for (MimeMessage message : messages) {
            receivers.addAll(Arrays.stream(message.getAllRecipients()).map(Address::toString).toList());
        }

        assertEquals(2, messages.length);
        assertTrue(messages[0].getContent().toString().contains("одобрен"));
        assertTrue(messages[1].getContent().toString().contains("одобрен"));
        assertTrue(receivers.contains(employee.getEmail()));
        assertTrue(receivers.contains(departmentBoss.getEmail()));
    }

    private VacationRequest setupVacation(Employee employee) {
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setEmployee(employee);
        vacationRequest.setVacationStartDate(LocalDate.now());
        vacationRequest.setVacationEndDate(LocalDate.now().plusDays(14));
        vacationRequest.setRequestStatus(RequestStatus.IN_PROCESS);

        return vacationRequest;
    }
}