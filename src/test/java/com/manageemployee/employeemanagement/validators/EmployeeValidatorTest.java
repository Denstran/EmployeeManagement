package com.manageemployee.employeemanagement.validators;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.validators.employeeValidators.EmployeeNewEntryValidator;
import com.manageemployee.employeemanagement.util.validators.employeeValidators.EmployeeUpdatingEntryValidator;
import com.manageemployee.employeemanagement.util.validators.employeeValidators.EmployeeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

public class EmployeeValidatorTest {
    private static final EmployeeService employeeService = Mockito.mock(EmployeeService.class);
    private static final PositionService positionService = Mockito.mock(PositionService.class);
    private static EmployeeValidator employeeValidator;
    private Employee employee;
    private EmployeeDTO dto;
    private DepartmentInfo departmentInfo;
    private BeanPropertyBindingResult bindingResult;

    @BeforeAll
    static void beforeAll() {
        MoneyService moneyService = new MoneyService();
        EmployeeNewEntryValidator newEntryValidator = new EmployeeNewEntryValidator(employeeService,
                positionService, moneyService);
        EmployeeUpdatingEntryValidator updatingEntryValidator =
                new EmployeeUpdatingEntryValidator(employeeService, moneyService, positionService);
        employeeValidator = new EmployeeValidator(List.of(newEntryValidator, updatingEntryValidator));
    }

    @BeforeEach
    void beforeEach() {
        dto = new EmployeeDTO();
            dto.setCompanyBranchId(1L);
            dto.setPositionId(1L);
            dto.setEmail("test@gmail.com");
            dto.setPhoneNumber("1111");

        employee = new Employee();
            employee.setId(1L);
            employee.setEmail(dto.getEmail());
            employee.setPhoneNumber(dto.getPhoneNumber());
            employee.setSalary(Money.getMoneyFromString("10000 RUB"));

        departmentInfo = new DepartmentInfo();
            departmentInfo.setDepartmentBudget(Money.getMoneyFromString("1000000 RUB"));

        bindingResult = new BeanPropertyBindingResult(dto, "dto");
        bindingResult = Mockito.spy(bindingResult);
    }

    @Test
    void assert_that_no_errors_when_inserting_new_entry() {
        Mockito.when(employeeService.existsByEmail(dto.getEmail())).thenReturn(false);
        Mockito.when(employeeService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(false);
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);

        dto.setSalary(Money.getMoneyFromString("1000 RUB"));
        employeeValidator.validate(dto, bindingResult);

        Assertions.assertFalse(bindingResult.hasErrors());
    }

    @Test
    void assert_that_has_errors_when_inserting_employee_with_not_unique_email_or_phone_number() {
        Mockito.when(employeeService.existsByEmail(dto.getEmail())).thenReturn(true);
        Mockito.when(employeeService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(true);
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);

        dto.setSalary(Money.getMoneyFromString("1000 RUB"));
        employeeValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasFieldErrors("email") && bindingResult.hasFieldErrors("phoneNumber"));
    }

    @Test
    void assert_that_has_errors_when_inserting_employee_with_higher_salary_than_department_budget() {
        Mockito.when(employeeService.existsByEmail(dto.getEmail())).thenReturn(false);
        Mockito.when(employeeService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(false);
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);

        dto.setSalary(Money.getMoneyFromString("100000000000000 RUB"));
        employeeValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasFieldErrors("salary"));
    }

    @Test
    void assert_that_has_errors_when_inserting_employee_with_salary_currency_different_from_department_currency() {
        Mockito.when(employeeService.existsByEmail(dto.getEmail())).thenReturn(false);
        Mockito.when(employeeService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(false);
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);

        dto.setSalary(Money.getMoneyFromString("100 EUR"));
        employeeValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasFieldErrors("salary"));
    }

    @Test
    void assert_that_if_bindingResult_contains_errors_then_returning_from_method() {
        bindingResult.addError(new FieldError("dto", "email", "message"));

        Mockito.when(employeeService.existsByEmail(dto.getEmail())).thenReturn(true);
        Mockito.when(employeeService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(true);
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);

        dto.setSalary(Money.getMoneyFromString("100 EUR"));
        employeeValidator.validate(dto, bindingResult);

        Mockito.verify(bindingResult, Mockito.never())
                .rejectValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void assert_that_no_errors_when_updating_entry() {
        dto.setId(1L);
        Mockito.when(employeeService.existsByEmail(dto.getEmail())).thenReturn(true);
        Mockito.when(employeeService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(true);
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);
        Mockito.when(employeeService.getById(dto.getId())).thenReturn(employee);

        dto.setSalary(Money.getMoneyFromString("1000 RUB"));

        employeeValidator.validate(dto, bindingResult);

        Assertions.assertFalse(bindingResult.hasErrors());
    }

    @Test
    void assert_that_errors_when_updating_entry_is_not_unique() {
        dto.setId(2L);
        Mockito.when(employeeService.getByEmail(dto.getEmail())).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.getByPhoneNumber(dto.getPhoneNumber())).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);
        Mockito.when(employeeService.getById(dto.getId())).thenReturn(employee);

        dto.setSalary(Money.getMoneyFromString("1000 RUB"));

        employeeValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasFieldErrors("email") &&
                bindingResult.hasFieldErrors("phoneNumber"));
    }

    @Test
    void assert_that_errors_when_updating_entry_salary_higher_than_department_budget() {
        dto.setId(1L);
        Mockito.when(employeeService.getByEmail(dto.getEmail())).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.getByPhoneNumber(dto.getPhoneNumber())).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);
        Mockito.when(employeeService.getById(dto.getId())).thenReturn(employee);

        dto.setSalary(Money.getMoneyFromString("1000000000000000 RUB"));
        employeeValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasFieldErrors("salary"));
    }

    @Test
    void assert_that_errors_when_updating_entry_salary_currency_different_from_department_budget_currency() {
        dto.setId(1L);
        Mockito.when(employeeService.getByEmail(dto.getEmail())).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.getByPhoneNumber(dto.getPhoneNumber())).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.getEmployeeDepartmentInfo(dto.getCompanyBranchId(), dto.getPositionId()))
                .thenReturn(departmentInfo);
        Mockito.when(employeeService.getById(dto.getId())).thenReturn(employee);

        dto.setSalary(Money.getMoneyFromString("1000 EUR"));
        employeeValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasFieldErrors("salary"));
    }
}
