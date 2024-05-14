package com.manageemployee.employeemanagement.task.validation;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.task.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TaskValidator implements Validator {
    private final EmployeeService employeeService;

    @Autowired
    public TaskValidator(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TaskDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TaskDTO dto = (TaskDTO) target;
        Employee taskOwner = employeeService.getById(dto.getTaskOwnerId());

        if (isFired(taskOwner))
            errors.reject("", "Нельзя выдать задачу уволенному сотруднику!");

        if (isOnVacation(taskOwner))
            errors.reject("", "Нельзя выдать задачу сотруднику в отпуске!");
    }

    private boolean isOnVacation(Employee taskOwner) {
        return EmployeeStatus.VACATION.equals(taskOwner.getEmployeeStatus());
    }

    private boolean isFired(Employee taskOwner) {
        return EmployeeStatus.FIRED.equals(taskOwner.getEmployeeStatus());
    }
}
