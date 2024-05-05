package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.employee.EmployeePaymentLogSpec;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.repository.EmployeePaymentLogRepository;
import com.manageemployee.employeemanagement.util.DateUtils;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.PaymentSpecificationProcessor;
import com.manageemployee.employeemanagement.util.enumType.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public Money getEmployeeAverageYearSalary(Employee employee, LocalDate endOfBillingPeriod) {
        LocalDate beginning = getBeginningOfBillingPeriod(employee, endOfBillingPeriod);
        List<EmployeePaymentLog> paymentLogs = getEmployeePaymentLogsBetweenDatesAndPaymentType(beginning,
                endOfBillingPeriod, employee, List.of(PaymentType.SALARY, PaymentType.BONUS));

        double result = countBillingResult(paymentLogs);
        return new Money(result);
    }

    private double countBillingResult(List<EmployeePaymentLog> paymentLogs) {
        return paymentLogs.stream()
                .mapToDouble(paymentLog -> paymentLog.getPaymentAmount().getAmount())
                .sum();
    }

    private LocalDate getBeginningOfBillingPeriod(Employee employee, LocalDate endOfBillingPeriod) {
        LocalDate beginning;
        LocalDate employmentDate = DateUtils.asLocalDate(employee.getEmploymentDate());

        if (employmentDate.getYear() == endOfBillingPeriod.getYear())
            beginning = employmentDate;
        else
            beginning = endOfBillingPeriod.minusYears(1L);

        return beginning;
    }

    public List<EmployeePaymentLog> getEmployeePaymentLogsBetweenDatesAndPaymentType(LocalDate beginning,
                                                                                     LocalDate end,
                                                                                     Employee employee,
                                                                                     List<PaymentType> paymentTypes) {
        return repository.findByDateOfPaymentBetweenAndEmployeeAndPaymentTypeIn(
                DateUtils.asDate(beginning), DateUtils.asDate(end), employee, paymentTypes);
    }

}
