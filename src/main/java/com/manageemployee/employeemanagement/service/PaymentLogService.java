package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.repository.CompanyBranchPaymentLogRepository;
import com.manageemployee.employeemanagement.repository.DepartmentInfoPaymentLogRepository;
import com.manageemployee.employeemanagement.repository.EmployeePaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentLogService {
    private final EmployeePaymentLogRepository employeePaymentLogRepository;
    private final CompanyBranchPaymentLogRepository companyBranchPaymentLogRepository;
    private final DepartmentInfoPaymentLogRepository departmentInfoPaymentLogRepository;

    @Autowired
    public PaymentLogService(EmployeePaymentLogRepository employeePaymentLogRepository,
                             CompanyBranchPaymentLogRepository companyBranchPaymentLogRepository,
                             DepartmentInfoPaymentLogRepository departmentInfoPaymentLogRepository) {
        this.employeePaymentLogRepository = employeePaymentLogRepository;
        this.companyBranchPaymentLogRepository = companyBranchPaymentLogRepository;
        this.departmentInfoPaymentLogRepository = departmentInfoPaymentLogRepository;
    }

    @Transactional
    public void saveEmployeePaymentLog(EmployeePaymentLog employeePaymentLog) {
        employeePaymentLogRepository.saveAndFlush(employeePaymentLog);
    }

    @Transactional
    public void saveCompanyBranchPaymentLog(CompanyBranchPaymentLog companyBranchPaymentLog) {
        companyBranchPaymentLogRepository.saveAndFlush(companyBranchPaymentLog);
    }

    @Transactional
    public void saveDepartmentInfoPaymentLog(DepartmentInfoPaymentLog departmentInfoPaymentLog) {
        departmentInfoPaymentLogRepository.saveAndFlush(departmentInfoPaymentLog);
    }
}
