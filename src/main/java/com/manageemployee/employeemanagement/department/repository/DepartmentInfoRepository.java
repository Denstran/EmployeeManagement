package com.manageemployee.employeemanagement.department.repository;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentInfoRepository extends JpaRepository<DepartmentInfo, CompanyBranchDepartmentPK> {
    List<DepartmentInfo> findByPk_CompanyBranch_Id(Long companyBranchId);
}
