package com.manageemployee.employeemanagement.task.repository;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTaskOwner(Employee taskOwner);
    List<Task> findAllByTaskOwner_Id(Long id);
    void deleteAllByTaskOwner_Id(Long employeeId);
}
