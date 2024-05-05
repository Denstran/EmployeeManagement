package com.manageemployee.employeemanagement.mail;

import java.time.LocalDate;

public interface VacationRequest {
    LocalDate getVacationStartDate();
    LocalDate getVacationEndDate();
    long getVacationDays();
    String getRequesterName();
    String getRequesterContacts();
}
