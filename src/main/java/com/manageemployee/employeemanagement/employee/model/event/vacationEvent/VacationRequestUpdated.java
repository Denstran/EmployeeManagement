package com.manageemployee.employeemanagement.employee.model.event.vacationEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.mail.VacationResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationRequestUpdated implements VacationResponse {
    private final boolean isChanged;
    private final LocalDate vacationStartDate;
    private final LocalDate vacationEndDate;
    private final Employee employee;

    @Override
    public boolean isChanged() {
        return isChanged;
    }

    @Override
    public LocalDate getVacationStartDate() {
        return vacationStartDate;
    }

    @Override
    public LocalDate getVacationEndDate() {
        return vacationEndDate;
    }
}
