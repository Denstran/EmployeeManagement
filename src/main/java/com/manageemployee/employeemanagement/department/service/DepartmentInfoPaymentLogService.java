package com.manageemployee.employeemanagement.department.service;

import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.repository.DepartmentInfoPaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentInfoPaymentLogService {
    private final DepartmentInfoPaymentLogRepository repository;

    @Autowired
    public DepartmentInfoPaymentLogService(DepartmentInfoPaymentLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveDepartmentInfoPaymentLog(DepartmentInfoPaymentLog departmentInfoPaymentLog) {
        repository.saveAndFlush(departmentInfoPaymentLog);
    }

    public List<DepartmentInfoPaymentLog> getDepartmentPayments(Long companyBranchId, Long departmentId,
                                                                String startDate, String endDate, String transferAction) {
        if (companyBranchId == null || companyBranchId <= 0 || departmentId == null || departmentId <= 0)
            throw new IllegalArgumentException("Выбран не существующий отдел!");

        Specification<DepartmentInfoPaymentLog> spec =
                Specification.where(DepartmentInfoPaymentLogSpec
                        .isEqualToCompanyBranchIdAndDepartmentId(companyBranchId, departmentId));

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty())
            spec = spec.and(DepartmentInfoPaymentLogSpec.isBetweenDates(startDate, endDate));

        if (transferAction != null && !transferAction.isEmpty() && !transferAction.equals("ALL"))
            spec = spec.and(DepartmentInfoPaymentLogSpec.isTransferActionEqualTo(transferAction));

        return repository.findAll(spec);
    }
}
