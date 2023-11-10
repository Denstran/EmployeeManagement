package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
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

    @Transactional
    public void addEmployee(Employee employee, Department department) {
        department.addEmployee(employee);
    }

    public void removeEmployee(Employee employee, Department department) {
        department.removeEmployee(employee);
        departmentRepository.saveAndFlush(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }
}
