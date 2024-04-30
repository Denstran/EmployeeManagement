package com.manageemployee.employeemanagement.mail;

import java.time.LocalDate;

public interface VacationResponse {
    boolean isChanged();
    LocalDate getVacationStartDate();
    LocalDate getVacationEndDate();
}
