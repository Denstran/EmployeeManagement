package com.manageemployee.employeemanagement.util.validators.employeeValidators;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PositionService;
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
    private final MoneyService moneyService;

    @Autowired
    public EmployeeNewEntryValidator(EmployeeService employeeService, PositionService positionService,
                                     MoneyService moneyService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.moneyService = moneyService;
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

        if (!dto.getSalary().getCurrency().equals(departmentInfo.getDepartmentBudget().getCurrency()))
            errors.rejectValue("salary", "", "Валюта не совпадает с валютой отдела!");

        if (moneyService.compareAmounts(departmentInfo.getDepartmentBudget(), dto.getSalary()) < 0)
            errors.rejectValue("salary", "", "Зарплата превышает бюджет отдела!");
    }

    private void validatePosition(EmployeeDTO dto, Errors errors) {
        Position position = positionService.getById(dto.getPositionId());
        if (!position.isLeading()) return;

        if (employeeService.existsByPositionAndCompanyBranch(position, dto.getCompanyBranchId()))
            errors.rejectValue("positionId", "", "Ведущая должность уже занята!");
    }
}
