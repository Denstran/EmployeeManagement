package com.manageemployee.employeemanagement.department.service;

import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.repository.DepartmentInfoPaymentLogRepository;
import com.manageemployee.employeemanagement.util.PaymentSpecificationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentInfoPaymentLogService {
    private final DepartmentInfoPaymentLogRepository repository;

    @Autowired
    public DepartmentInfoPaymentLogService(DepartmentInfoPaymentLogRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveDepartmentInfoPaymentLog(DepartmentInfoPaymentLog departmentInfoPaymentLog) {
        repository.saveAndFlush(departmentInfoPaymentLog);
    }

    public List<DepartmentInfoPaymentLog> getDepartmentPayments(Long companyBranchId, Long departmentId,
                                                                String startDate, String endDate, String transferAction) {
        validateResource(companyBranchId, departmentId);
        Specification<DepartmentInfoPaymentLog> spec =
                DepartmentInfoPaymentLogSpec.setupSpecification(companyBranchId, departmentId);
        spec = PaymentSpecificationProcessor.processDateParams(startDate, endDate, spec);
        spec = PaymentSpecificationProcessor.processAction(transferAction, spec);

        return repository.findAll(spec);
    }

    private void validateResource(Long companyBranchId, Long departmentId) {
        if (companyBranchId == null || companyBranchId <= 0 || departmentId == null || departmentId <= 0)
            throw new IllegalArgumentException("Выбран не существующий отдел!");
    }

}
