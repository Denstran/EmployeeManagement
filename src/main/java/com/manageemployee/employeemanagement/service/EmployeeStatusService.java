package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.EmployeeStatus;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.repository.EmployeeStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeStatusService {
    private final EmployeeStatusRepository employeeStatusRepository;

    @Autowired
    public EmployeeStatusService(EmployeeStatusRepository employeeStatusRepository) {
        this.employeeStatusRepository = employeeStatusRepository;
    }

    @Transactional
    public void addEmployee(Employee employee, EmployeeStatus employeeStatus) {
        employeeStatus.addEmployee(employee);
    }

    public void removeEmployee(Employee employee, EmployeeStatus employeeStatus) {
        employeeStatus.removeEmployee(employee);
        employeeStatusRepository.saveAndFlush(employeeStatus);
    }

    public EmployeeStatus createOrUpdateEmployeeStatus(EmployeeStatus employeeStatus) {
        return employeeStatusRepository.saveAndFlush(employeeStatus);
    }

    public EmployeeStatus getStatusByName(EEmployeeStatus employeeStatus) {
        return employeeStatusRepository.findEmployeeStatusByEmployeeStatus(employeeStatus);
    }

    public EmployeeStatus getEmployeeStatusById(Long id) {
        return employeeStatusRepository.findById(id).orElse(null);
    }

    public List<EmployeeStatus> getEmployeeStatuses() {
        return employeeStatusRepository.findAll();
    }

    public void deleteEmployeeStatusById(Long id) {
        employeeStatusRepository.deleteById(id);
    }

}
