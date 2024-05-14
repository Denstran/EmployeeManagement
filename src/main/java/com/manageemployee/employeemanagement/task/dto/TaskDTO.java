package com.manageemployee.employeemanagement.task.dto;

import com.manageemployee.employeemanagement.task.model.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.manageemployee.employeemanagement.task.model.Task}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TaskDTO implements Serializable {
    private Long id;
    @NotNull(message = "Описание задачи не может быть пустым!")
    @NotEmpty(message = "Описание задачи не может быть пустым!")
    private String taskDescription;
    private LocalDate taskCreated;
    @NotNull(message = "Срок выполнения задачи не должен отсутствовать!")
    @Future(message = "Срок выполнения задачи должен быть в будущем!")
    private LocalDate taskDeadLine;
    private TaskStatus taskStatus;

    @NotNull(message = "Сотрудник, получающий задачу, не может отсутствовать!")
    private Long taskOwnerId;
    @NotNull(message = "Сотрудник, выдающий задачу, не может отсутствовать!")
    private Long taskGiverId;

    private String employeeOwnerContacts;
    private String employeeGiverContacts;
}