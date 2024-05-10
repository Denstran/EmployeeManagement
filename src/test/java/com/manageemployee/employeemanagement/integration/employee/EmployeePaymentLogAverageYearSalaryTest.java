package com.manageemployee.employeemanagement.integration.employee;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.util.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
@Transactional
public class EmployeePaymentLogAverageYearSalaryTest {
    @Autowired
    private EmployeePaymentLogService paymentLogService;
    @Autowired
    private EmployeeService employeeService;

    @Test
    void should_return_correct_salary_when_called_on_old_employee() {
        Employee employee = employeeService.getById(1L);
        Money actualResult = paymentLogService.getEmployeeAverageYearSalary(employee, LocalDate.of(2024, 5, 5));
        Money expectedResult = new Money(75000.00);
        assertEquals(expectedResult, actualResult);
    }

}
