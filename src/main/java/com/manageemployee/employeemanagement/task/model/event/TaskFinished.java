package com.manageemployee.employeemanagement.task.model.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskFinished {
    private final String giverEmail;
    private final String ownerContacts;
    private final LocalDate finishDate;
    private final String taskDescription;
}
