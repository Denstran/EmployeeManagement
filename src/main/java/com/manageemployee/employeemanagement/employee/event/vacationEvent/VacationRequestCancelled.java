package com.manageemployee.employeemanagement.employee.event.vacationEvent;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import lombok.Data;

@Data
public class VacationRequestCancelled {
    private final Employee headOfDepartment;
    private final Employee vacationOwner;
}
