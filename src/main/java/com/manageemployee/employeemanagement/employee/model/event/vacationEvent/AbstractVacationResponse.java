package com.manageemployee.employeemanagement.employee.model.event.vacationEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.mail.VacationResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class AbstractVacationResponse implements VacationResponse {
    private final LocalDate vacationStartDate;
    private final LocalDate vacationEndDate;
    private final Employee employee;

    public abstract boolean isChanged();

    @Override
    public LocalDate getVacationStartDate() {
        return vacationStartDate;
    }

    @Override
    public LocalDate getVacationEndDate() {
        return vacationEndDate;
    }
}
