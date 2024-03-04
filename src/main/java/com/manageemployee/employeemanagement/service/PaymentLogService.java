package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.*;
import com.manageemployee.employeemanagement.repository.CompanyBranchPaymentLogRepository;
import com.manageemployee.employeemanagement.repository.DepartmentInfoPaymentLogRepository;
import com.manageemployee.employeemanagement.repository.EmployeePaymentLogRepository;
import com.manageemployee.employeemanagement.service.specs.CompanyBranchPaymentLogSpec;
import com.manageemployee.employeemanagement.service.specs.DepartmentInfoPaymentLogSpec;
import com.manageemployee.employeemanagement.service.specs.EmployeePaymentLogSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteEmployeePaymentLogs(Long employeeId) {
        employeePaymentLogRepository.deleteEmployeePaymentLogsByEmployee_Id(employeeId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteEmployeesPaymentLogsByPosition(Position position) {
        employeePaymentLogRepository.deleteAllByEmployee_Position(position);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteEmployeesPaymentLogsByCompanyBranchAndDepartment(CompanyBranch companyBranch,
                                                                       Department department) {
        employeePaymentLogRepository.deleteAllByEmployee_CompanyBranchAndEmployee_Position_Department(companyBranch, department);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteDepartmentInfoPaymentLogs(CompanyBranch companyBranch, Department department) {
        departmentInfoPaymentLogRepository.deleteAllByCompanyBranchAndDepartment(companyBranch, department);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteEmployeePaymentLogsByCompanyBranch(CompanyBranch companyBranch) {
        employeePaymentLogRepository.deleteAllByEmployee_CompanyBranch(companyBranch);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteDepartmentPaymentLogsByCompanyBranch(CompanyBranch companyBranch) {
        departmentInfoPaymentLogRepository.deleteAllByCompanyBranch(companyBranch);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteCompanyBranchPaymentLogsByCompanyBranch(CompanyBranch companyBranch) {
        companyBranchPaymentLogRepository.deleteAllByCompanyBranch(companyBranch);
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

        return companyBranchPaymentLogRepository.findAll(spec);
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

        return departmentInfoPaymentLogRepository.findAll(spec);
    }

    public List<EmployeePaymentLog> getEmployeePaymentLog(Long employeeId, String startDate,String endDate,
                                                          String transferAction) {
        if (employeeId == null || employeeId <= 0)
            throw new IllegalArgumentException("Выбранный сотрудник не существует!");

        Specification<EmployeePaymentLog> spec =
                Specification.where(EmployeePaymentLogSpec.isEqualToEmployeeId(employeeId));

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty())
            spec = spec.and(EmployeePaymentLogSpec.isBetweenDate(startDate, endDate));

        if (transferAction != null && !transferAction.isEmpty() && !transferAction.equals("ALL"))
            spec = spec.and(EmployeePaymentLogSpec.isTransferActionEqualTo(transferAction));

        return employeePaymentLogRepository.findAll(spec);
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

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty())
            spec = spec.and(EmployeePaymentLogSpec.isBetweenDate(startDate, endDate));

        if (transferAction != null && !transferAction.isEmpty() && !transferAction.equals("ALL"))
            spec = spec.and(EmployeePaymentLogSpec.isTransferActionEqualTo(transferAction));

        if (phoneNumber != null && !phoneNumber.isEmpty())
            spec = spec.and(EmployeePaymentLogSpec.isPhoneNumberEqualTo(phoneNumber));

        return employeePaymentLogRepository.findAll(spec);
    }
}
