package com.manageemployee.employeemanagement.mail;

import com.manageemployee.employeemanagement.util.Money;

import java.time.LocalDate;

public interface VacationRequest {
    LocalDate getVacationStartDate();
    LocalDate getVacationEndDate();
    int getVacationDays();
    Money getVacationMoney();
    String getRequesterName();
    String getRequesterContacts();
}
