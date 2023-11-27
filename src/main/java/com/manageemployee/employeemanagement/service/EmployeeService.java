package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throw new IllegalArgumentException("Illegal employee for saving! Should not be null!");

        employeeRepository.saveAndFlush(employee);
    }

    @Transactional
    public Employee updateEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException("Illegal employee for updating! Should not be null!");
        return employeeRepository.saveAndFlush(employee);
    }


    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getAllEmployeesInDepartment(Long depId) {
        return employeeRepository.findAllByDepartment_Id(depId);
    }

    @Transactional
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}
