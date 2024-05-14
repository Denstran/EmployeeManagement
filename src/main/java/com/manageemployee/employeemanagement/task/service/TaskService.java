package com.manageemployee.employeemanagement.task.service;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.task.model.Task;
import com.manageemployee.employeemanagement.task.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTaskByOwner(Employee owner) {
        if (owner == null)
            throw new NullPointerException("Передан не существующий сотрудник");
        return taskRepository.findAllByTaskOwner(owner);
    }

    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Transactional
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task createTask(Task entity) {
        if (entity == null)
            throw new NullPointerException("Передана не существующая задача");

        log.info("Saving task. Task before persistence: {}", entity);
        entity.createTask();
        entity = taskRepository.save(entity);
        log.info("Saving task. Task after persistence: {}", entity);
        return entity;
    }

    public void deleteAllByOwnerId(Long employeeId) {
        taskRepository.deleteAllByTaskOwner_Id(employeeId);
    }

    public List<Task> getTasksByOwnerId(Long employeeId) {
        return taskRepository.findAllByTaskOwner_Id(employeeId);
    }

    public boolean existsById(Long taskId) {
        return taskRepository.existsById(taskId);
    }
}
