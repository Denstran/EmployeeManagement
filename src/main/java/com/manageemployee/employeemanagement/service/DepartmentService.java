package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.exception.ResourceAlreadyExistsException;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CompanyBranchService companyBranchService;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, CompanyBranchService companyBranchService) {
        this.departmentRepository = departmentRepository;
        this.companyBranchService = companyBranchService;
    }

    public void createDepartment(Department department, CompanyBranch companyBranch) {
        Department departmentFromDB = findDepartmentByNameOrPhone(companyBranch.getId(), department.getDepartmentName(),
                department.getPhoneNumber());

        if (departmentFromDB != null) {
            throw new ResourceAlreadyExistsException("Отдел с таким названием или номером телефона уже существует");
        }

        companyBranchService.addDepartment(companyBranch, department);
    }

    public void updateDepartment(Department department, Long companyBranchId) {
        Department departmentFromDB = findDepartmentByNameOrPhone(companyBranchId, department.getDepartmentName(),
                department.getPhoneNumber());
        if (departmentFromDB != null && departmentFromDB.getId() != department.getId()) {
            throw new ResourceAlreadyExistsException("Отдел с таким названием или номером телефона уже существует");
        }


        departmentRepository.saveAndFlush(department);
    }

    public Department findDepartmentByNameOrPhone(Long companyBranchId,String depName, String depPhone) {
        return departmentRepository.findDepByNameOrPhoneAndCompanyBranch(companyBranchId, depName,
                depPhone).orElse(null);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public List<Department> getAllDepartmentsByCompanyBranchId(Long companyBranchId) {
        return departmentRepository.findByCompanyBranch_Id(companyBranchId);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }
}
