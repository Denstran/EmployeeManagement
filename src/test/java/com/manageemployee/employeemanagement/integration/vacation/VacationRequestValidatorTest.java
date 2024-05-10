package com.manageemployee.employeemanagement.integration.vacation;

import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.model.vacation.RequestStatus;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.service.VacationService;
import com.manageemployee.employeemanagement.employee.validation.vacation.VacationRequestNewEntryValidator;
import com.manageemployee.employeemanagement.employee.validation.vacation.VacationRequestUpdatingEntryValidator;
import com.manageemployee.employeemanagement.employee.validation.vacation.VacationValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
@AutoConfigureTestDatabase
public class VacationRequestValidatorTest {
    @Autowired
    private VacationService vacationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private VacationValidator vacationValidator;

    private List<VacationRequest> vacationRequests;

    @BeforeEach
    void beforeEach() {
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setVacationStartDate(LocalDate.now());
        vacationRequest.setVacationEndDate(LocalDate.now().plusDays(10));
        vacationRequest.setRequestStatus(RequestStatus.APPROVED);
        vacationRequest.setEmployee(employeeService.getReference(1L));

        vacationService.saveRequest(vacationRequest);
    }

    @AfterEach
    void afterEach() {
        vacationService.deleteById(1L);
    }

    @Test
    @Transactional
    void should_contain_no_errors_when_vacation_request_is_valid_when_creating() {
        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now().plusDays(100));
        dto.setVacationEndDate(LocalDate.now().plusDays(110));
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    @Transactional
    void should_contain_no_errors_when_has_a_lot_of_requested_vacations_if_still_has_vacation_days_left_when_creating() {
        vacationRequests = setupVacations();
        vacationService.saveAll(vacationRequests);

        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now().plusDays(100));
        dto.setVacationEndDate(LocalDate.now().plusDays(110));
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertFalse(errors.hasErrors());
    }

    // HERE
    @Test
    @Transactional
    void should_contain_no_errors_when_has_a_lot_of_requested_vacations_if_still_has_vacation_days_left_when_updating() {
        vacationRequests = setupVacations();
        vacationService.saveAll(vacationRequests);

        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now().plusDays(41));
        dto.setVacationEndDate(LocalDate.now().plusDays(51));
        dto.setRequestStatus(RequestStatus.IN_PROCESS);
        dto.setId(vacationRequests.get(1).getId());
        dto.setEmployeeId(1L);

        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    @Transactional
    void should_contain_error_when_has_a_lot_of_requested_vacations_if_still_has_vacation_days_left_when_creating() {
        vacationRequests = setupVacations();
        vacationService.saveAll(vacationRequests);

        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now().plusDays(100));
        dto.setVacationEndDate(LocalDate.now().plusDays(171));
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    @Transactional
    void should_contain_error_when_has_a_lot_of_requested_vacations_if_still_has_vacation_days_left_when_updating() {
        vacationRequests = setupVacations();
        vacationService.saveAll(vacationRequests);

        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now().plusDays(100));
        dto.setVacationEndDate(LocalDate.now().plusDays(1610));
        dto.setId(vacationRequests.get(0).getId());
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    @Transactional
    void should_contain_error_when_dates_are_not_valid_when_creating() {
        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now());
        dto.setVacationEndDate(LocalDate.now().plusDays(10));
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    @Transactional
    void should_contain_error_when_dates_are_not_valid_when_updating() {
        vacationRequests = setupVacations();
        vacationService.saveAll(vacationRequests);

        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now());
        dto.setVacationEndDate(LocalDate.now().plusDays(10));
        dto.setId(vacationRequests.get(0).getId());
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    @Transactional
    void should_contain_error_when_vacation_days_amount_more_then_employee_has_when_creating() {
        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setVacationStartDate(LocalDate.now().plusDays(100));
        dto.setVacationEndDate(LocalDate.now().plusDays(10000));
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        vacationValidator.validate(dto, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    @Transactional
    void should_not_be_validated_by_new_entry_validator_when_working_with_updating_entry() {
        VacationRequestNewEntryValidator newEntryValidator =
                new VacationRequestNewEntryValidator(employeeService, vacationService);
        newEntryValidator = spy(newEntryValidator);

        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setId(1L);
        dto.setVacationStartDate(LocalDate.now().plusDays(100));
        dto.setVacationEndDate(LocalDate.now().plusDays(10000));
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        errors = spy(errors);

        newEntryValidator.validate(dto, errors);
        verify(errors, never()).reject("", "Длина отпуска превышает количество отпускных дней!");
    }

    @Test
    @Transactional
    void should_not_be_validated_by_updating_entry_validator_when_working_with_new_entry() {
        VacationRequestUpdatingEntryValidator updatingEntryValidator
                = new VacationRequestUpdatingEntryValidator(employeeService, vacationService);
        updatingEntryValidator = spy(updatingEntryValidator);

        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setId(null);
        dto.setVacationStartDate(LocalDate.now().plusDays(100));
        dto.setVacationEndDate(LocalDate.now().plusDays(10000));
        dto.setEmployeeId(1L);
        Errors errors = new BeanPropertyBindingResult(dto, "dto");
        errors = spy(errors);

        updatingEntryValidator.validate(dto, errors);
        verify(errors, never()).reject("", "Длина отпуска превышает количество отпускных дней!");
    }

    private List<VacationRequest> setupVacations() {
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setVacationStartDate(LocalDate.now().plusDays(20));
        vacationRequest.setVacationEndDate(LocalDate.now().plusDays(30));
        vacationRequest.setRequestStatus(RequestStatus.IN_PROCESS);
        vacationRequest.setEmployee(employeeService.getReference(1L));

        VacationRequest vacationRequest2 = new VacationRequest();
        vacationRequest2.setVacationStartDate(LocalDate.now().plusDays(40));
        vacationRequest2.setVacationEndDate(LocalDate.now().plusDays(50));
        vacationRequest2.setRequestStatus(RequestStatus.IN_PROCESS);
        vacationRequest2.setEmployee(employeeService.getReference(1L));

        VacationRequest vacationRequest3 = new VacationRequest();
        vacationRequest3.setVacationStartDate(LocalDate.now().plusDays(60));
        vacationRequest3.setVacationEndDate(LocalDate.now().plusDays(70));
        vacationRequest3.setRequestStatus(RequestStatus.IN_PROCESS);
        vacationRequest3.setEmployee(employeeService.getReference(1L));

        return List.of(vacationRequest, vacationRequest2, vacationRequest3);
    }
}
