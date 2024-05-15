package com.manageemployee.employeemanagement.task.model.event;

import lombok.Data;

@Data
public class TaskCanceled {
    private final String taskOwnerEmail;
    private final String taskDescription;
    private final String taskGiverContacts;
}
