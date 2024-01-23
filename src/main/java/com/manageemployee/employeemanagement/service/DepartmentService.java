package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.repository.DepartmentRepository;
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

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public boolean existsByName(String depName) {
        return departmentRepository.existsByDepartmentName(depName);
    }

    public Optional<Department> findByName(String depName) {
        return departmentRepository.findDepartmentByDepartmentName(depName);
    }

    public Department getReference(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Выбранный отдел не существует!");

        return departmentRepository.getReferenceById(id);
    }
}
