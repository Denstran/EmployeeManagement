package com.manageemployee.employeemanagement.task.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.task.model.Task;
import com.manageemployee.employeemanagement.task.model.TaskStatus;
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

    @Transactional
    public Task finishTask(Task task) {
        task.finishTask();
        return taskRepository.save(task);
    }

    public void deleteAllByOwnerId(Long employeeId) {
        taskRepository.deleteAllByTaskOwner_Id(employeeId);
    }

    public List<Task> getTasksByOwnerId(Long employeeId) {
        return taskRepository.findAllByTaskOwner_Id(employeeId);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
                new IllegalArgumentException("Выбрана не существующая задача!"));
    }

    public List<Task> getAllGiveTasksByGiver(Employee giver) {
        if (giver == null)
            throw new NullPointerException("Передан не корректные аргументы");
        CompanyBranch giverBranch = giver.getCompanyBranch();
        Department giverDepartment = giver.getPosition().getDepartment();

        return taskRepository.findAllByCompanyBranchAndDepartment(giverBranch, giverDepartment);
    }

    @Transactional
    public Task approveTask(Task task) {
        if (task == null)
            throw new NullPointerException("Передан не корректные аргументы");
        if (task.getTaskStatus().equals(TaskStatus.FINISHED))
            return task;

        task.approve();
        return taskRepository.save(task);
    }
}
