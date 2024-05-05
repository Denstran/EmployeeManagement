package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VacationService {
    private final VacationRepository repository;

    @Autowired
    public VacationService(VacationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public VacationRequest saveRequest(VacationRequest vacationRequest) {
        if (vacationRequest == null) throw new NullPointerException("Передано null значение!");

        vacationRequest.createRequest();
        return repository.save(vacationRequest);
    }

    public List<VacationRequest> getAllVacations() {
        return repository.findAll();
    }

    public List<VacationRequest> getEmployeeVacations(Employee employee) {
        return repository.findByEmployee(employee);
    }
}
