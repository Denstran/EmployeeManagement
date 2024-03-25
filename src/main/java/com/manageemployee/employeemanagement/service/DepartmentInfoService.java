package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.repository.DepartmentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void allocateBudgetForSalary(DepartmentInfo departmentInfo, Money amountForAllocation) {
        departmentInfo.setDepartmentBudget(MoneyService.subtract(departmentInfo.getDepartmentBudget(),
                amountForAllocation));
        repository.saveAndFlush(departmentInfo);
    }

    @Transactional
    public void employeeSalaryReducing(DepartmentInfo departmentInfo, Money reducedSalary) {
        departmentInfo.setDepartmentBudget(MoneyService.sum(departmentInfo.getDepartmentBudget(), reducedSalary));
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

}
