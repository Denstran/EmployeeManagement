package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentInfoRepository extends JpaRepository<DepartmentInfo, CompanyBranchDepartmentPK> {
    void deleteAllByPk_CompanyBranch(CompanyBranch companyBranch);
    List<DepartmentInfo> findAllByPk_Department(Department department);
    void deleteAllByPk_Department(Department department);

}
