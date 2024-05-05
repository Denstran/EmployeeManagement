package com.manageemployee.employeemanagement.integration;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.service.VacationService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class VacationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VacationService vacationService;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("username", "secret"))
            .withPerMethodLifecycle(true);

    @Test
    @WithMockUser(username = "test@example.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_create_vacation_request_when_data_is_valid() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().plusDays(10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/vacations")); // Ожидаем, что произойдет редирект на указанный URL

        List<VacationRequest> vacationRequests = vacationService.getAllVacations();
        assertEquals(1, vacationRequests.size());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_send_email_when_saving_request() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().plusDays(10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/vacations")); // Ожидаем, что произойдет редирект на указанный URL

        TestTransaction.flagForCommit();
        TestTransaction.end();

        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertEquals("john.doe@examle.com", message.getAllRecipients()[0].toString());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"EMPLOYEE"})
    void should_return_creation_form_when_errors() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().minusDays(10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Ожидаем, что метод вернет статус 200
                .andExpect(MockMvcResultMatchers.view().name("vacation/createVacationForm")); // Ожидаем, что вернется представление для создания отпуска
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    void should_return_collection_with_one_element() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().plusDays(10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/vacations"));

        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/vacations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("vacationRequestDTOS"))
                .andExpect(MockMvcResultMatchers.model().attribute("vacationRequestDTOS", hasSize(1)));
    }
}
