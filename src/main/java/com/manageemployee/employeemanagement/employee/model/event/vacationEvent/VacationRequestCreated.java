package com.manageemployee.employeemanagement.employee.model.event.vacationEvent;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.model.employee.Name;
import com.manageemployee.employeemanagement.mail.VacationRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
public class VacationRequestCreated implements VacationRequest {
    private final CompanyBranch companyBranch;
    private final Department department;
    private final LocalDate vacationStartDate;
    private final LocalDate vacationEndDate;
    private final List<String> employeeContacts;
    private final Name employeeName;

    @Override
    public LocalDate getVacationStartDate() {
        return vacationStartDate;
    }

    @Override
    public LocalDate getVacationEndDate() {
        return vacationEndDate;
    }

    @Override
    public long getVacationDays() {
        return vacationStartDate.until(vacationEndDate, ChronoUnit.DAYS);
    }

    @Override
    public String getRequesterName() {
        return employeeName.toString();
    }

    @Override
    public String getRequesterContacts() {
        StringBuilder sb = new StringBuilder();
        for (String contract : employeeContacts)
            sb.append(contract).append("\n");

        return sb.toString();
    }
}
