package com.manageemployee.employeemanagement.task.model.event;

import lombok.Data;

@Data
public class TaskDisapproved {
    private final String ownerEmail;
    private final String taskDescription;
    private final String giverContacts;
}
