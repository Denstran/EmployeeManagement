package com.manageemployee.employeemanagement.department.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.repository.DepartmentInfoRepository;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentInfoService {
    private final DepartmentInfoRepository repository;
    private final CompanyBranchService companyBranchService;
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentInfoService(DepartmentInfoRepository repository, CompanyBranchService companyBranchService,
                                 DepartmentService departmentService) {
        this.repository = repository;
        this.companyBranchService = companyBranchService;
        this.departmentService = departmentService;
    }

    @Transactional
    public void create(DepartmentInfo departmentInfo) {
        departmentInfo.registerDepartmentInfo();
        repository.saveAndFlush(departmentInfo);
    }

    @Transactional
    public void update(DepartmentInfo departmentInfo) {
        DepartmentInfo departmentInfoFromDB = getById(departmentInfo.getPk());
        departmentInfo.updateDepartmentInfo(departmentInfoFromDB.getDepartmentBudget());

        repository.saveAndFlush(departmentInfo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void allocateBudgetForSalary(DepartmentInfo departmentInfo, Money amountForAllocation) {
        departmentInfo.setDepartmentBudget(MoneyUtil.subtract(departmentInfo.getDepartmentBudget(),
                amountForAllocation));
        repository.saveAndFlush(departmentInfo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void employeeSalaryReducing(DepartmentInfo departmentInfo, Money reducedSalary) {
        departmentInfo.setDepartmentBudget(MoneyUtil.sum(departmentInfo.getDepartmentBudget(), reducedSalary));
        repository.saveAndFlush(departmentInfo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteDepartmentInfo(DepartmentInfo departmentInfo) {
        departmentInfo.removeDepartmentInfo();

        repository.delete(departmentInfo);
    }

    public DepartmentInfo getById(CompanyBranchDepartmentPK id) {
        return repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Не верный данные для получения информации по отделу!"));
    }

    public DepartmentInfo getById(Long companyBranchId, Long depId) {
        CompanyBranch companyBranch = companyBranchService.getReference(companyBranchId);
        Department department = departmentService.getReference(depId);

        return getById(new CompanyBranchDepartmentPK(companyBranch, department));
    }

    public boolean existsById(CompanyBranchDepartmentPK id) {
        return repository.existsById(id);
    }

    public List<DepartmentInfo> getDepartmentsByCompanyBranchId(Long companyBranchId) {
        if (companyBranchId == null || companyBranchId <= 0)
            throw new IllegalArgumentException("Выбран не существующий отдел!");

        return repository.findByPk_CompanyBranch_Id(companyBranchId);
    }
}
