package com.manageemployee.employeemanagement.task.repository;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTaskOwner(Employee taskOwner);
    List<Task> findAllByTaskOwner_Id(Long id);

    @Query("SELECT t FROM Task t WHERE t.taskOwner.companyBranch = :companyBranch " +
            "AND t.taskOwner.position.department = :department")
    List<Task> findAllByCompanyBranchAndDepartment(@Param("companyBranch") CompanyBranch companyBranch,
                                                   @Param("department") Department department);
    void deleteAllByTaskOwner_Id(Long employeeId);
}
