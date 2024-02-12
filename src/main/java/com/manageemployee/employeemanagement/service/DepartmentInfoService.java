package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.repository.DepartmentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        DepartmentInfo departmentInfoFromDB = repository.findById(departmentInfo.getPk())
                .orElseThrow(() -> new IllegalArgumentException("Выбранный отдел не существует!"));

        departmentInfo.updateDepartmentInfo(departmentInfoFromDB.getDepartmentBudget());

        repository.saveAndFlush(departmentInfo);
    }

    @Transactional
    public void removeDepartment(DepartmentInfo departmentInfo) {
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

        return repository.findById(new CompanyBranchDepartmentPK(companyBranch, department)).orElseThrow(() ->
                new IllegalArgumentException("Не верный данные для получения информации по отделу!"));
    }

    public boolean existsById(CompanyBranchDepartmentPK id) {
        return repository.existsById(id);
    }

}
