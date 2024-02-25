package com.manageemployee.employeemanagement.util.validators.employeeValidators;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@ValidatorQualifier(validatorKey = "employeeSubValidator")
public class EmployeeNewEntryValidator implements Validator {
    private final EmployeeService employeeService;
    private final MoneyService moneyService;

    @Autowired
    public EmployeeNewEntryValidator(EmployeeService employeeService, MoneyService moneyService) {
        this.employeeService = employeeService;
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

        if (employeeService.existsByEmail(dto.getEmail()))
            errors.rejectValue("email","", "Сотрудник с такой почтой уже нанят!");

        if (employeeService.existsByPhoneNumber(dto.getPhoneNumber()))
            errors.rejectValue("phoneNumber","", "Сотрудник с таким номером телефона уже нанят!");

        DepartmentInfo departmentInfo = employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(),
                dto.getPositionId());

        if (!dto.getSalary().getCurrency().equals(departmentInfo.getDepartmentBudget().getCurrency()))
            errors.rejectValue("salary", "", "Валюта не совпадает с валютой отдела!");

        if (moneyService.compareAmounts(departmentInfo.getDepartmentBudget(), dto.getSalary()) < 0)
            errors.rejectValue("salary", "", "Зарплата превышает бюджет отдела!");

    }
}
