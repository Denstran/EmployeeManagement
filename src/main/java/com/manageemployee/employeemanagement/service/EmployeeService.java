package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.*;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import com.manageemployee.employeemanagement.model.enumTypes.EmployeeStatus;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

        if (employee.getEmployeeStatus().equals(EmployeeStatus.FIRED)) {
            employee.setSalary(employeeFromDB.getSalary());
            employee.fireEmployee();
        }
        else {
            employee.updateEmployee(employeeFromDB);
        }
        repository.saveAndFlush(employee);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteEmployee(Employee employee) {
        employee.deleteEmployee();
        repository.delete(employee);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAllByCompanyBranch(CompanyBranch companyBranch) {
        repository.deleteAllByCompanyBranch(companyBranch);
    }

    @Transactional
    public void deleteAllByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department) {
        repository.deleteAllByCompanyBranchAndPosition_Department(companyBranch, department);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAllByPosition(Position position) {
        List<Employee> employees = getByPosition(position);
        employees.forEach(this::deleteEmployee);
        repository.deleteAllByPosition(position);
    }

    public List<Employee> getByPosition(Position position) {
        return repository.findByPosition(position);
    }

    public String getEmployeeNameById(Long employeeId) {
        Optional<Name> employeeName = repository.getEmployeeNameById(employeeId);
        return employeeName.map(Name::toString).orElseThrow(()
                -> new IllegalArgumentException("Выбранного сотрудника не существует"));
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
