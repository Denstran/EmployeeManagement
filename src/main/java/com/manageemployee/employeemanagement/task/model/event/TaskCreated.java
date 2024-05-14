package com.manageemployee.employeemanagement.task.model.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreated {
    private final String employeeEmail;
    private final String taskDescription;
    private final String giverContacts;
    private final LocalDate taskDeadLine;
}
