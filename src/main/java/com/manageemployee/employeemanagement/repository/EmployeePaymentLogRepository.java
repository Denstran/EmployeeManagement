package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.EmployeePaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeePaymentLogRepository extends JpaRepository<EmployeePaymentLog, Long>,
        JpaSpecificationExecutor<EmployeePaymentLog> {
    List<EmployeePaymentLog> findAllByEmployee_Id(Long employeeId);

    List<EmployeePaymentLog> findAllByEmployee_CompanyBranch_IdAndEmployee_Position_Department_Id(Long companyBranchId,
                                                                                                     Long departmentId);
    void deleteAllByEmployee_CompanyBranch(CompanyBranch companyBranch);
    void deleteEmployeePaymentLogsByEmployee_Id(Long employeeId);
    void deleteAllByEmployee_CompanyBranchAndEmployee_Position_Department(CompanyBranch companyBranch,
                                                                             Department department);
}
