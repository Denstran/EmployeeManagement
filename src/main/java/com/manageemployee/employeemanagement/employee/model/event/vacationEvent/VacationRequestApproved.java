package com.manageemployee.employeemanagement.employee.model.event.vacationEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;

import java.time.LocalDate;

public class VacationRequestApproved extends AbstractVacationResponse {

    public VacationRequestApproved(LocalDate vacationStartDate, LocalDate vacationEndDate, Employee employee) {
        super(vacationStartDate, vacationEndDate, employee);
    }

    @Override
    public boolean isChanged() {
        return false;
    }

}
