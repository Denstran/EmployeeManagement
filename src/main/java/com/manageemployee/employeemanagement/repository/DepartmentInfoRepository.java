package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentInfoRepository extends JpaRepository<DepartmentInfo, CompanyBranchDepartmentPK> {
    void deleteAllByPk_CompanyBranch(CompanyBranch companyBranch);
}
