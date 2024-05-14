package com.manageemployee.employeemanagement.task.model;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.task.model.event.TaskCreated;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDate;

@Entity
@Table(name = "TASK")
@Getter
@Setter
public class Task extends AbstractAggregateRoot<Task> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TASK_DESCRIPTION")
    @NotNull(message = "Описание задачи не может быть пустым!")
    @NotEmpty(message = "Описание задачи не может быть пустым!")
    private String taskDescription;

    @Column(name = "TASK_CREATED", updatable = false)
    @Temporal(value = TemporalType.DATE)
    @NotNull(message = "Дата выдачи задачи не может отсутствовать!")
    private LocalDate taskCreated;

    @Column(name = "TASK_DEADLINE")
    @Temporal(value = TemporalType.DATE)
    @Future(message = "Срок выполнения задачи должен быть в будущем!")
    @NotNull(message = "Срок выполнения задачи не должен отсутствовать!")
    private LocalDate taskDeadLine;

    @Column(name = "TASK_STATUS")
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Статус задачи не может отсутствовать!")
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "EMPLOYEE_OWNER_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_EMPLOYEE_OWNER_TASK"))
    private Employee taskOwner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "EMPLOYEE_GIVER_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_EMPLOYEE_GIVER_TASK"))
    private Employee taskGiver;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskCreated=" + taskCreated +
                ", taskDeadLine=" + taskDeadLine +
                ", taskStatus=" + taskStatus +
                ", taskOwner=" + taskOwner +
                ", taskGiver=" + taskGiver +
                '}';
    }

    public void createTask() {
        taskCreated = LocalDate.now();
        taskStatus = TaskStatus.IN_PROCESS;
        registerEvent(new TaskCreated(
                getTaskOwner().getEmail(),
                taskDescription,
                getTaskGiver().getEmployeeContacts(),
                taskDeadLine));
    }
}
