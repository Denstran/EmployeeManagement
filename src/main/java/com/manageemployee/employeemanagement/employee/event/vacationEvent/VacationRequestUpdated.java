package com.manageemployee.employeemanagement.employee.event.vacationEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;

import java.time.LocalDate;

public class VacationRequestUpdated extends AbstractVacationResponse {
    public VacationRequestUpdated(LocalDate vacationStartDate, LocalDate vacationEndDate, Employee employee) {
        super(vacationStartDate, vacationEndDate, employee);
    }

    @Override
    public boolean isChanged() {
        return true;
    }

}
