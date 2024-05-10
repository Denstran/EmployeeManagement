package com.manageemployee.employeemanagement.integration.employee;

import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.Name;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.position.service.PositionService;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import com.manageemployee.employeemanagement.util.Address;
import com.manageemployee.employeemanagement.util.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CompanyBranchService companyBranchService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void afterEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "employee_payment_log", "employee");
    }

    @Test
    void employee_saved_correctly() {
        createEmployee(2L);

        Employee employee = employeeService.getByEmail("test@email.com").orElse(null);
        Assertions.assertNotNull(employee);
    }

    @Test
    void employee_has_leading_and_employee_roles_after_creation() {
        createEmployee(1L);

        Employee employee = employeeService.getByEmail("test@email.com").orElse(null);
        Assertions.assertNotNull(employee);

        Set<UserRole> roles = getUserRole(employee.getUser());

        Assertions.assertTrue(roles.contains(UserRole.ROLE_HEAD_OF_DEPARTMENT)
                && roles.contains(UserRole.ROLE_EMPLOYEE));
    }

    @Test
    void employee_has_employee_role_after_changing_position() {
        createEmployee(1L);
        Employee employee = employeeService.getByEmail("test@email.com").orElse(null);
        Assertions.assertNotNull(employee);

        employee.setPosition(positionService.getReference(2L));
        employeeService.updateEmployee(employee);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        employee = employeeService.getByEmail("test@email.com").orElse(null);
        Set<UserRole> roles = getUserRole(employee.getUser());
        Assertions.assertTrue(roles.contains(UserRole.ROLE_EMPLOYEE)
                && !roles.contains(UserRole.ROLE_HEAD_OF_DEPARTMENT));
        TestTransaction.end();
    }

    @Test
    void employee_has_HR_role_after_creating_with_HR_position() {
        createEmployee(12L);
        Employee employee = employeeService.getByEmail("test@email.com").orElse(null);
        Assertions.assertNotNull(employee);

        Set<UserRole> roles = getUserRole(employee.getUser());
        Assertions.assertTrue(roles.contains(UserRole.ROLE_HR));
    }

    @Test
    void employee_has_HR_role_and_leading_role_after_creating_with_leading_HR_position() {
        createEmployee(11L);

        Employee employee = employeeService.getByEmail("test@email.com").orElse(null);
        Assertions.assertNotNull(employee);

        Set<UserRole> roles = getUserRole(employee.getUser());
        roles.forEach(System.out::println);
        Assertions.assertTrue(roles.contains(UserRole.ROLE_HR)
                && roles.contains(UserRole.ROLE_HEAD_OF_DEPARTMENT));
    }

    @Test
    void employee_has_only_hr_role_after_changing_position_from_leading() {
        createEmployee(11L);
        Employee employee = employeeService.getByEmail("test@email.com").orElse(null);
        Assertions.assertNotNull(employee);

        employee.setPosition(positionService.getReference(12L));
        employeeService.updateEmployee(employee);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        employee = employeeService.getByEmail("test@email.com").orElse(null);
        Set<UserRole> roles = getUserRole(employee.getUser());
        Assertions.assertTrue(roles.contains(UserRole.ROLE_HR)
                && !roles.contains(UserRole.ROLE_HEAD_OF_DEPARTMENT));

        TestTransaction.end();
    }

    private Set<UserRole> getUserRole(User user) {
        return  user.getAuthorities().stream()
                .map(grantedAuthority -> UserRole.valueOf(grantedAuthority.getAuthority()))
                .collect(Collectors.toSet());
    }

    protected void createEmployee(Long positionId) {
        Employee employee = new Employee();
        Name name = new Name("Test", "Test", "test");
        Address address = new Address("test", "test", "test", 1, "test");
        Money salary = new Money(1000.0);
        String email = "test@email.com";
        String phone = "+79818339627";

        employee.setName(name);
        employee.setHomeAddress(address);
        employee.setSalary(salary);
        employee.setEmail(email);
        employee.setPhoneNumber(phone);
        employee.setPosition(positionService.getReference(positionId));
        employee.setCompanyBranch(companyBranchService.getReference(1L));
        employeeService.createEmployee(employee);
    }
}
