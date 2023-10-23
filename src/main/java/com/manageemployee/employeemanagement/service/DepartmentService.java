package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        companyBranchService.addDepartment(companyBranch, department);
    }

    public void updateDepartment(Department department) {

        departmentRepository.saveAndFlush(department);
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
