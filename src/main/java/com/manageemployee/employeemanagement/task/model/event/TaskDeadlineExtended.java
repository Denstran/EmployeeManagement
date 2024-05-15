package com.manageemployee.employeemanagement.task.model.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDeadlineExtended {
    private final String ownerEmail;
    private final String taskDescription;
    private final String giverContacts;
    private final LocalDate extendedDeadline;
}
