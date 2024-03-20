package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.repository.DepartmentInfoRepository;
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
    public void deleteDepartmentInfo(DepartmentInfo departmentInfo) {
        departmentInfo.removeDepartmentInfo();

        repository.delete(departmentInfo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAllByCompanyBranch(CompanyBranch companyBranch) {
        repository.deleteAllByPk_CompanyBranch(companyBranch);
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAllByDepartment(Department department) {
        List<DepartmentInfo> departmentInfos = getByDepartment(department);
        departmentInfos.forEach(this::deleteDepartmentInfo);
        repository.deleteAllByPk_Department(department);
    }

    private List<DepartmentInfo> getByDepartment(Department department) {
        return repository.findAllByPk_Department(department);
    }

    public boolean existsById(CompanyBranch companyBranch, Department department) {
        return existsById(new CompanyBranchDepartmentPK(companyBranch, department));
    }
}
