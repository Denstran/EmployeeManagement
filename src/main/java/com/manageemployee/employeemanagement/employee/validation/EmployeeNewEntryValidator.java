package com.manageemployee.employeemanagement.employee.validation;

import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.position.service.PositionService;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@ValidatorQualifier(validatorKey = "employeeSubValidator")
public class EmployeeNewEntryValidator implements Validator {
    private final EmployeeService employeeService;
    private final PositionService positionService;

    @Autowired
    public EmployeeNewEntryValidator(EmployeeService employeeService, PositionService positionService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmployeeDTO dto = (EmployeeDTO) target;
        if (dto.getId() != null) return;
        if (errors.hasErrors()) return;

        validateUniqueValues(dto, errors);
        validateMoney(dto, errors);
        validatePosition(dto, errors);
    }

    private void validateUniqueValues(EmployeeDTO dto, Errors errors) {
        if (employeeService.existsByEmail(dto.getEmail()))
            errors.rejectValue("email","", "Сотрудник с такой почтой уже нанят!");

        if (employeeService.existsByPhoneNumber(dto.getPhoneNumber()))
            errors.rejectValue("phoneNumber","", "Сотрудник с таким номером телефона уже нанят!");
    }

    private void validateMoney(EmployeeDTO dto, Errors errors) {
        DepartmentInfo departmentInfo = employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(),
                dto.getPositionId());

        if (Money.compareTo(departmentInfo.getDepartmentBudget(), dto.getSalary()) < 0)
            errors.rejectValue("salary", "", "Зарплата превышает бюджет отдела!");
    }

    private void validatePosition(EmployeeDTO dto, Errors errors) {
        Position position = positionService.getById(dto.getPositionId());
        if (!position.isLeading()) return;

        if (employeeService.existsByPositionAndCompanyBranch(position, dto.getCompanyBranchId()))
            errors.rejectValue("positionId", "", "Ведущая должность уже занята!");
    }
}
