package com.manageemployee.employeemanagement.companyBranch.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.companyBranch.repository.CompanyBranchPaymentLogRepository;
import com.manageemployee.employeemanagement.util.PaymentSpecificationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyBranchPaymentLogService {
    private final CompanyBranchPaymentLogRepository repository;

    @Autowired
    public CompanyBranchPaymentLogService(CompanyBranchPaymentLogRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCompanyBranchPaymentLog(CompanyBranchPaymentLog companyBranchPaymentLog) {
        repository.saveAndFlush(companyBranchPaymentLog);
    }

    public List<CompanyBranchPaymentLog> getCompanyBranchPayments(Long companyBranchId, String startDate,
                                                                  String endDate, String transferAction) {
        validateResource(companyBranchId);

        Specification<CompanyBranchPaymentLog> spec = CompanyBranchPaymentLogSpec.setupSpecification(companyBranchId);
        spec = PaymentSpecificationProcessor.processDateParams(startDate, endDate, spec);
        spec = PaymentSpecificationProcessor.processAction(transferAction, spec);

        return repository.findAll(spec);
    }

    private void validateResource(Long companyBranchId) {
        if (companyBranchId == null || companyBranchId <= 0)
            throw new IllegalArgumentException("Выбран не существующий филиал!");
    }
}
