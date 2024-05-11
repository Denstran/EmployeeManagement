package com.manageemployee.employeemanagement.integration.vacation;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.VacationRequestMapper;
import com.manageemployee.employeemanagement.employee.model.vacation.RequestStatus;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class VacationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VacationService vacationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private VacationRequestMapper vacationRequestMapper;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("username", "secret"))
            .withPerMethodLifecycle(true);

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_create_vacation_request_when_data_is_valid() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().plusDays(10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/vacations")); // Ожидаем, что произойдет редирект на указанный URL

        List<VacationRequest> vacationRequests = vacationService.getAllVacations();
        assertEquals(1, vacationRequests.size());

        vacationService.deleteAllByEmployeeEmail("john.doe@examle.com");
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    @Transactional
    void should_send_email_when_saving_request() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().plusDays(10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/vacations")); // Ожидаем, что произойдет редирект на указанный URL

        TestTransaction.flagForCommit();
        TestTransaction.end();

        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertEquals("john.doe@examle.com", message.getAllRecipients()[0].toString());
        vacationService.deleteAllByEmployeeEmail("john.doe@examle.com");
    }

    @Test
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    void should_return_creation_form_when_errors() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().minusDays(10L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(status().isOk()) // Ожидаем, что метод вернет статус 200
                .andExpect(MockMvcResultMatchers.view().name("vacation/createVacationForm"));// Ожидаем, что вернется представление для создания отпуска
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    void should_return_collection_with_one_element() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now().plusDays(100L));
        vacationRequestDTO.setVacationEndDate(LocalDate.now().plusDays(110L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/vacations"));

        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/vacations"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("vacationRequestDTOS"))
                .andExpect(MockMvcResultMatchers.model().attribute("vacationRequestDTOS", hasSize(1)));

        vacationService.deleteAllByEmployeeEmail("john.doe@examle.com");
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"EMPLOYEE"})
    void should_return_creation_form_when_errors_with_vacation_dates() throws Exception {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        vacationRequestDTO.setEmployeeId(1L);
        vacationRequestDTO.setVacationStartDate(LocalDate.now());
        vacationRequestDTO.setVacationEndDate(LocalDate.now().plusDays(1000000000L));

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/requestVacation")
                        .flashAttr("vacationRequestDTO", vacationRequestDTO))
                .andExpect(status().isOk()) // Ожидаем, что метод вернет статус 200
                // Ожидаем, что вернется представление для создания отпуска
                .andExpect(MockMvcResultMatchers.view().name("vacation/createVacationForm"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@exale.com", roles = {"EMPLOYEE"})
    void should_be_thrown_exception_when_trying_to_access_another_employee_vacation_when_updating_and_getting_from()
            throws Exception {
        VacationRequest vacation = setUpVacation(2L);
        vacation = vacationService.saveRequest(vacation);

        VacationRequest finalVacation = vacation;
        assertThatThrownBy(() -> {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/myPage/vacations/" + finalVacation.getId() + "/update"))
                    .andExpect(status().isInternalServerError());
        }).hasCauseInstanceOf(SecurityException.class);

        vacationService.deleteById(vacation.getId());
    }

    @Test
    @WithMockUser(username = "john.doe@exmple.com", roles = {"EMPLOYEE"})
    @Transactional
    void assert_that_no_exception_thrown_with_correct_user_when_updating_and_getting_form() throws Exception {
        VacationRequest vacation = setUpVacation(2L);
        vacation = vacationService.saveRequest(vacation);

        performGetWhenUpdating(vacation.getId());

        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@examle.com")
    void assert_that_no_exception_thrown_with_head_of_department_when_updating_and_getting_form() throws Exception {
        VacationRequest vacation = setUpVacation(3L);
        vacation = vacationService.saveRequest(vacation);

        performGetWhenUpdating(vacation.getId());

        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@exmple.com")
    void asser_that_vacation_being_updated_with_correct_user_and_data() throws Exception {
        VacationRequest vacation = setUpVacation(2L);
        vacation = vacationService.saveRequest(vacation);

        VacationRequestDTO dto = vacationRequestMapper.toDto(vacation);
        dto.setVacationEndDate(LocalDate.now().plusDays(15));

       performPostWhenUpdatingWithRedirect(dto);

        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(LocalDate.now().plusDays(15), vacation.getVacationEndDate());

        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@examle.com")
    void asser_that_vacation_being_updated_with_head_of_department_and_correct_data() throws Exception {
        VacationRequest vacation = setUpVacation(2L);
        vacation = vacationService.saveRequest(vacation);

        VacationRequestDTO dto = vacationRequestMapper.toDto(vacation);
        dto.setVacationEndDate(LocalDate.now().plusDays(15));

        performPostWhenUpdatingWithRedirect(dto);

        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(LocalDate.now().plusDays(15), vacation.getVacationEndDate());

        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@exmple.com")
    void assert_exception_is_thrown_when_illegal_access() throws Exception {
        VacationRequest vacation = setUpVacation(3L);
        vacation = vacationService.saveRequest(vacation);

        VacationRequestDTO dto = vacationRequestMapper.toDto(vacation);
        dto.setVacationEndDate(LocalDate.now().plusDays(15));

        assertThatThrownBy(() -> {
            performPostWhenUpdatingWithRedirect(dto);
        }).hasCauseInstanceOf(SecurityException.class);

        // Проверка на то, что данные не изменились
        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(LocalDate.now().plusDays(10), vacation.getVacationEndDate());

        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@examle.com")
    void assert_that_contains_error_when_invalid_data_with_correct_user() throws Exception {
        VacationRequest vacation = setUpVacation(1L);
        vacationService.saveRequest(vacation);

        VacationRequestDTO dto = vacationRequestMapper.toDto(vacation);
        dto.setVacationEndDate(LocalDate.now().minusDays(10));

        performPostWhenUpdatingWithErrors(dto);

        // Проверка на то, что данные не изменены
        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(LocalDate.now().plusDays(10), vacation.getVacationEndDate());

        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@examle.com")
    void assert_that_contains_error_when_invalid_data_after_validator_with_correct_user() throws Exception {
        VacationRequest vacation = setUpVacation(1L);
        VacationRequest vacation2 = setUpVacation(1L);
        vacation2.setVacationStartDate(LocalDate.now().plusDays(11));
        vacation2.setVacationEndDate(LocalDate.now().plusDays(21));
        vacationService.saveAll(List.of(vacation, vacation2));

        VacationRequestDTO dto = vacationRequestMapper.toDto(vacation);
        dto.setVacationStartDate(LocalDate.now().plusDays(11));
        dto.setVacationEndDate(LocalDate.now().plusDays(21));

        performPostWhenUpdatingWithErrors(dto);

        // Проверка, что данные не изменились
        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(10, vacation.getVacationDays());

        vacationService.deleteById(1L);
        vacationService.deleteById(2L);
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    void assert_that_vacation_is_approved_when_correct_role() throws Exception {
        VacationRequest vacation = setUpVacation(1L);
        vacation = vacationService.saveRequest(vacation);

        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/" + vacation.getId() + "/approve")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/vacations"));

        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(RequestStatus.APPROVED, vacation.getRequestStatus());
        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@gail.com", roles = {"HEAD_OF_DEPARTMENT"})
    void should_throw_exception_when_not_correct_department() {
        VacationRequest vacation = setUpVacation(1L);
        vacation = vacationService.saveRequest(vacation);

        final VacationRequest finalVacation = vacation;
        assertThatThrownBy(() -> {
            performPostWhenApproving(finalVacation);
        }).hasCauseInstanceOf(SecurityException.class);

        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(RequestStatus.IN_PROCESS, vacation.getRequestStatus());
        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doeAnotherBranch@examle.com", roles = {"HEAD_OF_DEPARTMENT"})
    void should_throw_exception_when_not_correct_company_branch() {
        VacationRequest vacation = setUpVacation(1L);
        vacation = vacationService.saveRequest(vacation);

        final VacationRequest finalVacation = vacation;
        assertThatThrownBy(() -> {
            performPostWhenApproving(finalVacation);
        }).hasCauseInstanceOf(SecurityException.class);

        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(RequestStatus.IN_PROCESS, vacation.getRequestStatus());
        vacationService.deleteById(vacation.getId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "john.doe@examle.com", roles = {"EMPLOYEE"})
    void should_throw_exception_when_not_correct_role() throws Exception {
        VacationRequest vacation = setUpVacation(1L);
        vacation = vacationService.saveRequest(vacation);

        final VacationRequest finalVacation = vacation;
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/" + finalVacation.getId() + "/approve")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isForbidden()); // Ожидаем, что метод вернет редирект

        vacation = vacationService.getVacationById(vacation.getId());
        assertEquals(RequestStatus.IN_PROCESS, vacation.getRequestStatus());
        vacationService.deleteById(vacation.getId());
    }

    private void performPostWhenApproving(VacationRequest vacationRequest) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/" + vacationRequest.getId() + "/approve")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/vacations"));
    }

    private void performGetWhenUpdating(Long vacationId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/vacations/" + vacationId + "/update"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("vacation/updateVacationForm"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("vacationRequestDTO"));
    }

    private void performPostWhenUpdatingWithRedirect(VacationRequestDTO dto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/" + dto.getId() + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", dto))
                .andExpect(status().is3xxRedirection()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.redirectedUrl("/myPage/vacations"));
    }

    private void performPostWhenUpdatingWithErrors(VacationRequestDTO dto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/vacations/" + dto.getId() + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("vacationRequestDTO", dto))
                .andExpect(status().isOk()) // Ожидаем, что метод вернет редирект
                .andExpect(MockMvcResultMatchers.view().name("vacation/updateVacationForm"));
    }

    private VacationRequest setUpVacation(Long employeeId) {
        VacationRequest vacation = new VacationRequest();
        vacation.setEmployee(employeeService.getReference(employeeId));
        vacation.setRequestStatus(RequestStatus.IN_PROCESS);
        vacation.setVacationStartDate(LocalDate.now());
        vacation.setVacationEndDate(LocalDate.now().plusDays(10));
        return vacation;
    }
}
