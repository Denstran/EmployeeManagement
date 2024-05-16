package com.manageemployee.employeemanagement.task.dto;

import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskExtensionDeadlineDTO {
    private Long taskId;
    @Future(message = "Изменить срок сдачи задачи можно только на будущую дату!")
    private LocalDate extendedDeadline;
}
