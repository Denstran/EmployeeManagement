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

    public List<DepartmentInfoPaymentLog> getDepartmentPayments(Long companyBranchId, String departmentId,
                                                                String startDate, String endDate, String transferAction) {
        validateResource(companyBranchId);
        Specification<DepartmentInfoPaymentLog> spec = setupSpec(companyBranchId, departmentId);
        spec = PaymentSpecificationProcessor.processDateParams(startDate, endDate, spec);
        spec = PaymentSpecificationProcessor.processAction(transferAction, spec);

        return repository.findAll(spec);
    }

    private Specification<DepartmentInfoPaymentLog> setupSpec(Long companyBranchId, String departmentId) {
        if (departmentId == null || departmentId.isEmpty() || departmentId.equals("ALL"))
            return DepartmentInfoPaymentLogSpec.setupSpecification(companyBranchId);

        try {
            Long depId = Long.parseLong(departmentId);
            return DepartmentInfoPaymentLogSpec.setupSpecification(companyBranchId, depId);
        }catch (NumberFormatException e) {
            return DepartmentInfoPaymentLogSpec.setupSpecification(companyBranchId);
        }
    }

    private void validateResource(Long companyBranchId) {
        if (companyBranchId == null || companyBranchId <= 0)
            throw new IllegalArgumentException("Выбран не существующий отдел!");
    }

}
