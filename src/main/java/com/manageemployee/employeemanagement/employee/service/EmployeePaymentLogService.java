package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.employee.EmployeePaymentLogSpec;
import com.manageemployee.employeemanagement.employee.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.repository.EmployeePaymentLogRepository;
import com.manageemployee.employeemanagement.util.PaymentSpecificationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeePaymentLogService {
    private final EmployeePaymentLogRepository repository;

    @Autowired
    public EmployeePaymentLogService(EmployeePaymentLogRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEmployeePaymentLog(EmployeePaymentLog employeePaymentLog) {
        repository.save(employeePaymentLog);
    }

    public List<EmployeePaymentLog> getEmployeePaymentLog(Long employeeId, String startDate, String endDate,
                                                          String transferAction) {
        validateResources(employeeId);
        Specification<EmployeePaymentLog> spec = EmployeePaymentLogSpec.setupSpecification(employeeId);
        spec = PaymentSpecificationProcessor.processDateParams(startDate, endDate, spec);
        spec = PaymentSpecificationProcessor.processAction(transferAction, spec);

        return repository.findAll(spec);
    }

    public List<EmployeePaymentLog> getAllEmployeesPaymentLogsFormDepartment(Long companyBranchId, Long departmentId,
                                                                             String startDate,String endDate,
                                                                             String transferAction,
                                                                             String phoneNumber) {
        validateResources(companyBranchId, departmentId);
        Specification<EmployeePaymentLog> spec =
                EmployeePaymentLogSpec.setupSpecification(companyBranchId, departmentId);
        spec = PaymentSpecificationProcessor.processDateParams(startDate, endDate, spec);
        spec = PaymentSpecificationProcessor.processAction(transferAction, spec);
        spec = processPhoneNumberParameter(phoneNumber, spec);

        return repository.findAll(spec);
    }

    private Specification<EmployeePaymentLog> processPhoneNumberParameter(String phoneNumber,
                                                                          Specification<EmployeePaymentLog> spec) {
        if (!isValidPhoneNumber(phoneNumber)) return spec;

        return spec.and(EmployeePaymentLogSpec.isPhoneNumberEqualTo(phoneNumber));
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && !phoneNumber.isEmpty();
    }

    private void validateResources(Long...resources) {
        for (Long resource : resources)
            checkResourceValidity(resource);
    }

    private void checkResourceValidity(Long resource) {
        if (resource == null || resource <= 0)
            throw new IllegalArgumentException("Выбран не существующий ресурс!");
    }
}
