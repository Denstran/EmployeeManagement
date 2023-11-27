package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void saveEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException("Невалидный сотрудник для сохранения!");

        employeeRepository.saveAndFlush(employee);
    }

    @Transactional
    public void updateEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException("Невалидный сотрудник для обновления!");
        employeeRepository.saveAndFlush(employee);
    }


    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Выбран несуществующий сотрудник"));
    }

    public List<Employee> getAllEmployeesInDepartment(Long depId) {
        if (depId == null || depId < 1)
            throw new IllegalArgumentException("Выбран несуществующий отдел!");

        return employeeRepository.findAllByDepartment_Id(depId);
    }

    @Transactional
    public void deleteEmployeeById(Long id) {
        if (id == null || id < 1)
            throw new IllegalArgumentException("Выбран несуществующий сотрудник!");

        employeeRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByCompanyBranchId(Long companyBranchId) {
        if (companyBranchId == null || companyBranchId < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");

        employeeRepository.deleteAllByCompanyBranchId(companyBranchId);
    }

    @Transactional
    public void deleteAllByDepartment(Department department) {
        if (department == null) throw new IllegalArgumentException("Выбран несуществующий отдел!");

        employeeRepository.deleteAllByDepartment(department);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty())
            return false;

        return employeeRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean existsByEmail(String email) {
        if (email == null || email.isEmpty())
            return false;

        return employeeRepository.existsByEmail(email);
    }

    public Optional<Employee> findByPhoneNumber(String phoneNumber) {
        return employeeRepository.findEmployeeByPhoneNumber(phoneNumber);
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }
}
