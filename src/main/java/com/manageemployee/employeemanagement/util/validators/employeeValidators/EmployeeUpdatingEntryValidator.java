package com.manageemployee.employeemanagement.util.validators.employeeValidators;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@ValidatorQualifier(validatorKey = "employeeSubValidator")
public class EmployeeUpdatingEntryValidator implements Validator {
    private final EmployeeService employeeService;
    private final MoneyService moneyService;
    private final PositionService positionService;

    @Autowired
    public EmployeeUpdatingEntryValidator(EmployeeService employeeService, MoneyService moneyService,
                                          PositionService positionService) {
        this.employeeService = employeeService;
        this.moneyService = moneyService;
        this.positionService = positionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmployeeDTO dto = (EmployeeDTO) target;
        if (dto.getId() == null) return;
        if (errors.hasErrors()) return;

        validateUniqueValues(dto, errors);
        validateMoney(dto, errors);
        validatePosition(dto, errors);
    }

    private void validateUniqueValues(EmployeeDTO dto, Errors errors) {
        Optional<Employee> employeeByEmail = employeeService.getByEmail(dto.getEmail());
        Optional<Employee> employeeByPhoneNumber = employeeService.getByPhoneNumber(dto.getPhoneNumber());

        if (employeeByEmail.isPresent() && !employeeByEmail.get().getId().equals(dto.getId()))
            errors.rejectValue("email", "", "Сотрудник с такой почтой уже нанят!");

        if (employeeByPhoneNumber.isPresent() && !employeeByPhoneNumber.get().getId().equals(dto.getId()))
            errors.rejectValue("phoneNumber", "", "Сотрудник с таким номером телефона уже нанят!");
    }

    private void validateMoney(EmployeeDTO dto, Errors errors) {
        DepartmentInfo departmentInfo = employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(),
                dto.getPositionId());

        if (!dto.getSalary().getCurrency().equals(departmentInfo.getDepartmentBudget().getCurrency())) {
            errors.rejectValue("salary", "", "Валюта не совпадает с валютой отдела!");
            return;
        }
        Employee employee = employeeService.getById(dto.getId());
        Money salaryIncrease = moneyService.subtract(dto.getSalary(), employee.getSalary());

        if (moneyService.compareAmounts(departmentInfo.getDepartmentBudget(), salaryIncrease) < 0)
            errors.rejectValue("salary", "", "Надбавка превышает бюджет отдела!");
    }

    private void validatePosition(EmployeeDTO dto, Errors errors) {
        Position position = positionService.getById(dto.getPositionId());
        if (!position.isLeading()) return;

        Optional<Employee> employee =
                employeeService.getByPositionAndCompanyBranchId(position, dto.getCompanyBranchId());

        if (employee.isEmpty()) return;

        if (!employee.get().getId().equals(dto.getId()))
            errors.rejectValue("positionId", "", "Ведущая должность уже занята!");
    }
}
