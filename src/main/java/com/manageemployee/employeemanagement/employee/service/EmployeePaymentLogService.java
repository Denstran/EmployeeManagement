package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.employee.EmployeePaymentLogSpec;
import com.manageemployee.employeemanagement.employee.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.repository.EmployeePaymentLogRepository;
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
        if (employeeId == null || employeeId <= 0)
            throw new IllegalArgumentException("Выбранный сотрудник не существует!");

        Specification<EmployeePaymentLog> spec =
                Specification.where(EmployeePaymentLogSpec.isEqualToEmployeeId(employeeId));

        processDateParams(startDate, endDate, spec);

        if (transferAction != null && !transferAction.isEmpty() && !transferAction.equals("ALL"))
            spec = spec.and(EmployeePaymentLogSpec.isTransferActionEqualTo(transferAction));

        return repository.findAll(spec);
    }

    public List<EmployeePaymentLog> getAllEmployeesPaymentLogsFormDepartment(Long companyBranchId, Long departmentId,
                                                                             String startDate,String endDate,
                                                                             String transferAction,
                                                                             String phoneNumber) {
        if (companyBranchId == null || companyBranchId <= 0 || departmentId == null || departmentId <= 0)
            throw new IllegalArgumentException("Выбран не существующий отдел!");

        Specification<EmployeePaymentLog> spec =
                Specification.where
                        (EmployeePaymentLogSpec.isEqualToCompanyBranchIdAndDepartmentId(companyBranchId, departmentId));

        spec = processDateParams(startDate, endDate, spec);

        if (transferAction != null && !transferAction.isEmpty() && !transferAction.equals("ALL"))
            spec = spec.and(EmployeePaymentLogSpec.isTransferActionEqualTo(transferAction));

        if (phoneNumber != null && !phoneNumber.isEmpty())
            spec = spec.and(EmployeePaymentLogSpec.isPhoneNumberEqualTo(phoneNumber));

        return repository.findAll(spec);
    }

    private Specification<EmployeePaymentLog> processDateParams(String startDate, String endDate,
                                                                Specification<EmployeePaymentLog> spec) {
        if (!isDateParamsValid(startDate, endDate)) return spec;

        return spec.and(EmployeePaymentLogSpec.isBetweenDate(startDate, endDate));
    }

    private boolean isDateParamsValid(String startDate, String endDate) {
        return startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty();
    }
}
