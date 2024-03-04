package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeePaymentLogRepository extends JpaRepository<EmployeePaymentLog, Long>,
        JpaSpecificationExecutor<EmployeePaymentLog> {
    void deleteAllByEmployee_Position(Position position);
    void deleteAllByEmployee_CompanyBranch(CompanyBranch companyBranch);
    void deleteEmployeePaymentLogsByEmployee_Id(Long employeeId);
    void deleteAllByEmployee_CompanyBranchAndEmployee_Position_Department(CompanyBranch companyBranch,
                                                                             Department department);

    void deleteAllByEmployee_Position_Department(Department department);
}
