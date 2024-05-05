package com.manageemployee.employeemanagement.integration;

import com.manageemployee.employeemanagement.employee.dto.SearchEmployeeFilters;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
public class EmployeeServiceSpecificationTest {
    @Autowired
    private EmployeeService employeeService;
    private static SearchEmployeeFilters filters;

    @BeforeAll
    static void setUp() {
        filters = new SearchEmployeeFilters(1L, 1L);
    }

    @AfterEach
    void reset() {
        filters = new SearchEmployeeFilters(1L, 1L);
    }

    @Test
    void should_return_all_employees() {
        List<Employee> employees = employeeService.getAllEmployee(filters);

        Assertions.assertEquals(11, employees.size());
    }

    @Test
    void should_return_one_employee_when_search_by_email() {
        filters.setEmail("john.doe@examle.com");
        List<Employee> employees = employeeService.getAllEmployee(filters);

        Assertions.assertEquals(1, employees.size());
    }

    @Test
    void should_return_no_employee_when_search_by_different_email_and_phone_number() {
        filters.setEmail("john.doe@examle.com");
        filters.setPhoneNumber("555-123-4561");
        List<Employee> employees = employeeService.getAllEmployee(filters);

        Assertions.assertTrue(employees.isEmpty());
    }

    @Test
    void should_return_one_employee_when_searching_by_same_email_and_phone_number() {
        filters.setEmail("john.doe@examle.com");
        filters.setPhoneNumber("555-123-4567");
        List<Employee> employees = employeeService.getAllEmployee(filters);

        Assertions.assertEquals(1, employees.size());
    }

    @Test
    void should_return_one_employee_when_status_selected() {
        filters.setEmployeeStatus(EmployeeStatus.FIRED.name());
        List<Employee> employees = employeeService.getAllEmployee(filters);

        Assertions.assertEquals(1, employees.size());
    }

    @Test
    void should_return_three_employees_with_salary_between() {
        filters.setSalaryStart(1000.0);
        filters.setSalaryEnd(30000.0);

        List<Employee> employees = employeeService.getAllEmployee(filters);
        Assertions.assertEquals(2, employees.size());
    }

    @Test
    void should_return_two_employees_when_searching_with_salary_between_and_vacation_status() {
        filters.setEmployeeStatus(EmployeeStatus.VACATION.name());
        filters.setSalaryStart(1000.0);
        filters.setSalaryEnd(30000.0);

        List<Employee> employees = employeeService.getAllEmployee(filters);
        Assertions.assertEquals(1, employees.size());
    }

    @Test
    void should_return_zero_employees_when_searching_with_salary_between_and_working_status() {
        filters.setEmployeeStatus(EmployeeStatus.WORKING.name());
        filters.setSalaryStart(1000.0);
        filters.setSalaryEnd(30000.0);

        List<Employee> employees = employeeService.getAllEmployee(filters);
        Assertions.assertEquals(0, employees.size());
    }

    @Test
    void should_return_six_employees_when_searching_between_two_and_four_working_years() {
        filters.setStartAmountOfWorkingYears(2);
        filters.setEndAmountOfWorkingYears(4);

        List<Employee> employees = employeeService.getAllEmployee(filters);
        Assertions.assertEquals(6, employees.size());
    }

    @Test
    void should_return_one_employee_when_searching_by_position() {
        filters.setPositionName("Marketing Manager");
        List<Employee> employees = employeeService.getAllEmployee(filters);
        Assertions.assertEquals(1, employees.size());
    }
}
