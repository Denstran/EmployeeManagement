package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfoPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DepartmentInfoPaymentLogRepository extends JpaRepository<DepartmentInfoPaymentLog, Long>,
        JpaSpecificationExecutor<DepartmentInfoPaymentLog> {
    List<DepartmentInfoPaymentLog> findDepartmentInfoPaymentLogByCompanyBranch_IdAndDepartment_Id(Long companyBranchId,
                                                                                                  Long departmentId);

    void deleteAllByCompanyBranch(CompanyBranch companyBranch);
    void deleteAllByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department);

    void deleteAllByDepartment(Department department);
}
