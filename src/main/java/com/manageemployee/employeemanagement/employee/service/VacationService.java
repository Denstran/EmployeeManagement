package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.employee.model.VacationRequest;
import com.manageemployee.employeemanagement.employee.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VacationService {
    private final VacationRepository repository;

    @Autowired
    public VacationService(VacationRepository repository) {
        this.repository = repository;
    }

    public VacationRequest saveRequest(VacationRequest vacationRequest) {
        if (vacationRequest == null) throw new NullPointerException("Передано null значение!");

        vacationRequest.createRequest();
        return repository.save(vacationRequest);
    }
}
