package com.manageemployee.employeemanagement.employee.validation.vacation;

import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.service.VacationService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@ValidatorQualifier(validatorKey = "vacationSubValidator")
@Component
@Slf4j
public class VacationRequestNewEntryValidator implements Validator {
    private final EmployeeService employeeService;
    private final VacationService vacationService;

    public VacationRequestNewEntryValidator(EmployeeService employeeService, VacationService vacationService) {
        this.employeeService = employeeService;
        this.vacationService = vacationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return VacationRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VacationRequestDTO vacationRequestDTO = (VacationRequestDTO) target;
        if (!isNewEntry(vacationRequestDTO)) return;
        log.info("VALIDATING CREATION OF VACATION_REQUEST");
        validateVacationDays(vacationRequestDTO, errors);
        validateVacationDates(vacationRequestDTO, errors);
        log.info("VALIDATION FINISHED. ERRORS: {} TOTAL COUNT: {}", errors.getAllErrors(), errors.getErrorCount());
    }

    private void validateVacationDates(VacationRequestDTO vacationRequestDTO, Errors errors) {
        if (vacationDatesInExistingVacation(vacationRequestDTO))
            errors.reject("", "Даты отпуска пересекаются с уже существующим отпуском!");
    }

    private boolean vacationDatesInExistingVacation(VacationRequestDTO vacationRequestDTO) {
        LocalDate startDate = vacationRequestDTO.getVacationStartDate();
        LocalDate endDate = vacationRequestDTO.getVacationEndDate();
        Long employeeId = vacationRequestDTO.getEmployeeId();

        log.info("VALIDATING REQUESTED DATE FOR VACATION: START DATE {} END DATE {}", startDate, endDate);
        return vacationService.existsByEmployeeIdAndVacationDates(employeeId, startDate, endDate);
    }

    private void validateVacationDays(VacationRequestDTO dto, Errors errors) {
        Employee employee = employeeService.getById(dto.getEmployeeId());
        long allRequestedVacationsDaysAmount = vacationService.getVacationDaysOfProcessingRequestByEmployee(employee);
        long employeeVacationDaysLeft = employee.getVacationDays() - allRequestedVacationsDaysAmount;
        long vacationDaysAmount = dto.getVacationDaysAmount();
        log.info("VALIDATION VACATION_DAYS FOR EMPLOYEE {}, TOTAL REQUESTED VACATION DAYS: {}," +
                " TOTAL EMPLOYEE VACATION DAYS LEFT {}, REQUESTED VACATION DAYS AMOUNT: {}",
                employee, allRequestedVacationsDaysAmount, employeeVacationDaysLeft, vacationDaysAmount);

        if (vacationDaysAmount > employeeVacationDaysLeft)
            errors.reject("",
                    "Количество дней отпуска превышает количество оставшихся отпускных дней!\n" +
                            "Суммарное количество запрошенных отпускных дней: " + allRequestedVacationsDaysAmount + "\n" +
                            "Количество оставшихся отпускных дней: " + employeeVacationDaysLeft);
    }


    private boolean isNewEntry(VacationRequestDTO vacationRequestDTO) {
        return vacationRequestDTO.getId() == null;
    }
}
