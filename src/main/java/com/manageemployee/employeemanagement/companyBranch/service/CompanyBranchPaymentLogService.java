package com.manageemployee.employeemanagement.companyBranch.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.companyBranch.repository.CompanyBranchPaymentLogRepository;
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
        if (companyBranchId == null || companyBranchId <= 0)
            throw new IllegalArgumentException("Выбран не существующий филиал!");

        Specification<CompanyBranchPaymentLog> spec =
                Specification.where(CompanyBranchPaymentLogSpec.isIdEqual(companyBranchId));

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty())
            spec = spec.and(CompanyBranchPaymentLogSpec.isBetweenDates(startDate, endDate));

        if (transferAction != null && !transferAction.isEmpty() && !transferAction.equals("ALL"))
            spec = spec.and(CompanyBranchPaymentLogSpec.isTransferActionEqualTo(transferAction));

        return repository.findAll(spec);
    }
}
