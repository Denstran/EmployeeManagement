package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.*;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final MoneyService moneyService;

    @Autowired
    public EmployeeService(EmployeeRepository repository, MoneyService moneyService) {
        this.repository = repository;
        this.moneyService = moneyService;
    }

    @Transactional
    public void createEmployee(Employee employee) {
        employee.hireEmployee();
        repository.saveAndFlush(employee);
    }

    @Transactional
    public void updateEmployee(Employee employee) {
        Employee employeeFromDB = getById(employee.getId());

        if (employee.getEmployeeStatus().getEmployeeStatus().equals(EEmployeeStatus.FIRED)) {
            employee.setSalary(employeeFromDB.getSalary());
            employee.fireEmployee();
        }
        else {
            employee.updateEmployee(employeeFromDB);
        }
        repository.saveAndFlush(employee);
    }

    @Transactional
    public void deleteEmployee(Employee employee) {
        employee.deleteEmployee();
        repository.delete(employee);
    }

    @Transactional
    public void deleteAllByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department) {
        repository.deleteAllByCompanyBranchAndPosition_Department(companyBranch, department);
    }

    public List<Employee> getEmployeesByCompanyBranchAndDepartment(Long companyBranchId, Long depId) {
        return repository.getEmployeeByCompanyBranchAndDepartment(companyBranchId, depId);
    }

    public DepartmentInfo getEmployeeDepartmentInfo(Long companyBranchId, Long employeePositionId) {
        return repository.findEmployeeDepartmentInfo(companyBranchId, employeePositionId).orElseThrow(() ->
                new IllegalArgumentException("Не удалось найти отдел по входным данным!"));
    }

    public Employee getById(Long employeeId) {
        return repository.findById(employeeId).orElseThrow(() ->
                new IllegalArgumentException("Выбранного сотрудника не существует!"));
    }

    public Optional<Employee> getByEmail(String email) {
        return repository.findEmployeeByEmail(email);
    }

    public Optional<Employee> getByPhoneNumber(String phoneNumber) {
        return repository.findEmployeeByPhoneNumber(phoneNumber);
    }

    public Money countEmployeeSalariesByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department) {
        List<Employee> employees =
                repository.findWorkingEmployeesByCompanyBranchAndDepartment(companyBranch, department);

        Money totalResult = new Money(BigDecimal.ZERO, companyBranch.getBudget().getCurrency());
        for (Employee employee : employees)
            totalResult = moneyService.sum(totalResult, employee.getSalary());

        return totalResult;
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }
}
