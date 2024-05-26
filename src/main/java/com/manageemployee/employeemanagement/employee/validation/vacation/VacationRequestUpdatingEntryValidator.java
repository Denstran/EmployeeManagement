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

@Component
@ValidatorQualifier(validatorKey = "vacationSubValidator")
@Slf4j
public class VacationRequestUpdatingEntryValidator implements Validator {
    private final EmployeeService employeeService;
    private final VacationService vacationService;

    public VacationRequestUpdatingEntryValidator(EmployeeService employeeService, VacationService vacationService) {
        this.employeeService = employeeService;
        this.vacationService = vacationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return VacationRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VacationRequestDTO dto = (VacationRequestDTO) target;
        if (isNewEntry(dto)) return;
        log.info("VALIDATING UPDATE OF VACATION_REQUEST");
        validateVacationDays(dto, errors);
        validateVacationDates(dto, errors);
        log.info("VALIDATION FINISHED. ERRORS: {} TOTAL COUNT: {}", errors.getAllErrors(), errors.getErrorCount());
    }

    private void validateVacationDates(VacationRequestDTO dto, Errors errors) {
        if (vacationDatesInExistingVacation(dto))
            errors.reject("", "Даты отпуска пересекаются с уже существующим отпуском!");
    }

    private boolean vacationDatesInExistingVacation(VacationRequestDTO dto) {
        LocalDate startDate = dto.getVacationStartDate();
        LocalDate endDate = dto.getVacationEndDate();
        Long employeeId = dto.getEmployeeId();

        log.info("VALIDATING REQUESTED DATE FOR VACATION: START DATE {} END DATE {}", startDate, endDate);
        return vacationService.existsByEmployeeIdAndVacationDatesExcludeVacationNotCancelled(employeeId, startDate,
                endDate, dto.getId());
    }

    private void validateVacationDays(VacationRequestDTO dto, Errors errors) {
        Employee employee = employeeService.getById(dto.getEmployeeId());
        long allRequestedVacationsDaysAmount =
                vacationService.getVacationDaysOfProcessingRequestByEmployeeExcludeCurrentVacation(employee,
                                                                                                   dto.getId());
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

    private boolean isNewEntry(VacationRequestDTO dto) {
        return dto.getId() == null;
    }
}
