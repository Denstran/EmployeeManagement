package com.manageemployee.employeemanagement.department.service;

import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public void createDepartment(Department department) {
        departmentRepository.saveAndFlush(department);
    }

    public Department getById(Long depId) {
        return departmentRepository.findById(depId).orElseThrow(() ->
                new IllegalArgumentException("Выбран не существующий отдел!"));
    }

    public List<Department> getAvailableDepartments(Long companyBranchId) {
        if (companyBranchId == null || companyBranchId <= 0)
            throw new IllegalArgumentException("Выбранный филиал не существует!");
        return departmentRepository.getAvailableDepartments(companyBranchId);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public boolean existsByName(String depName) {
        return departmentRepository.existsByDepartmentName(depName);
    }

    public Optional<Department> getByName(String depName) {
        return departmentRepository.findDepartmentByDepartmentName(depName);
    }

    public Department getByPositionName(String positionName) {
        return departmentRepository.findByPositionName(positionName);
    }

    public Department getReference(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Выбранный отдел не существует!");

        return departmentRepository.getReferenceById(id);
    }

    public boolean existsByType(DepartmentType departmentType) {
        if (departmentType == null) return false;
        return departmentRepository.existsByDepartmentType(departmentType);
    }
}
