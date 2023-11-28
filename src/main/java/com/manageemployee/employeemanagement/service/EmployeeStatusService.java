package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.EmployeeStatus;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.repository.EmployeeStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeStatusService {
    private final EmployeeStatusRepository employeeStatusRepository;

    @Autowired
    public EmployeeStatusService(EmployeeStatusRepository employeeStatusRepository) {
        this.employeeStatusRepository = employeeStatusRepository;
    }

    public EmployeeStatus createOrUpdateEmployeeStatus(EmployeeStatus employeeStatus) {
        return employeeStatusRepository.saveAndFlush(employeeStatus);
    }

    public EmployeeStatus getStatusByName(EEmployeeStatus employeeStatus) {
        return employeeStatusRepository.findEmployeeStatusByEmployeeStatus(employeeStatus);
    }

    public EmployeeStatus getEmployeeStatusReferenceById(Long id) {
        return employeeStatusRepository.getReferenceById(id);
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
