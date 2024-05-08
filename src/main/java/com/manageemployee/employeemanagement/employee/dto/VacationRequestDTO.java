package com.manageemployee.employeemanagement.employee.dto;

import com.manageemployee.employeemanagement.employee.model.vacation.RequestStatus;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * DTO for {@link VacationRequest}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VacationRequestDTO implements Serializable {
    private Long id;
    @NotNull(message = "Сотрудник не должен отсутствовать!")
    private Long employeeId;
    @FutureOrPresent(message = "Дата начала отпуска не может быть в прошлом!")
    private LocalDate vacationStartDate;
    @Future(message = "Дата конца отпуска должна быть в будущем!")
    private LocalDate vacationEndDate;
    private RequestStatus requestStatus;

    public long getVacationDaysAmount() {
        return vacationStartDate.until(vacationEndDate, ChronoUnit.DAYS);
    }
}