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

    @Transactional
    public void updateDepartment(Department department) {
        if (department == null)
            throw new IllegalArgumentException("Не валидный отдел для обновления!");

        departmentRepository.saveAndFlush(department);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Выбран не существующий отдел!"));
    }

    public List<Department> getAllDepartmentsByCompanyBranchId(Long companyBranchId) {
        if (companyBranchId == null || companyBranchId < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");

        return departmentRepository.findByCompanyBranch_Id(companyBranchId);
    }

    @Transactional
    public void saveDepartment(Department department) {
        if (department == null)
            throw new IllegalArgumentException("Не валидный отдел для сохранения!");

        departmentRepository.saveAndFlush(department);
    }

    public Department getDepartmentReferenceById(Long depId) {
        if (depId == null || depId < 1)
            throw new IllegalArgumentException("Выбран несуществующий отдел!");

        return departmentRepository.getReferenceById(depId);
    }

    @Transactional
    public void deleteDepartment(Department department) {
        if (department == null)
            throw new IllegalArgumentException("Не валидный отдел для удаления!");

        departmentRepository.deleteAllEmployeesByDepartment(department);
        departmentRepository.deleteAllPositionsByDepartment(department);
        departmentRepository.delete(department);
    }

    public Optional<CompanyBranch> findCompanyBranchByDepartmentId(Long depId) {
        return departmentRepository.findCompanyBranchByDepartmentId(depId);
    }

    public boolean existsByPhoneNumberAndCompanyBranchId(String phoneNumber, Long companyBranchId) {
        if (companyBranchId == null || companyBranchId < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");

        return departmentRepository.existsByPhoneNumberAndCompanyBranch_Id(phoneNumber, companyBranchId);
    }

    public boolean existsByDepartmentNameAndCompanyBranchId(String depName, Long companyBranchId) {
        if (companyBranchId == null || companyBranchId < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");

        return departmentRepository.existsByDepartmentNameAndCompanyBranch_Id(depName, companyBranchId);
    }

    public Optional<Department> findDepartmentByPhoneNumberAndCompanyBranch_Id(String phoneNumber,
                                                                               Long companyBranchId) {
        if (companyBranchId == null || companyBranchId < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");

        return departmentRepository.findDepartmentByPhoneNumberAndCompanyBranch_Id(phoneNumber, companyBranchId);
    }

    public Optional<Department> findDepartmentByDepartmentNameAndCompanyBranch_Id(String name, Long companyBranchId) {
        if (companyBranchId == null || companyBranchId < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");

        return departmentRepository.findDepartmentByDepartmentNameAndCompanyBranch_Id(name, companyBranchId);
    }
}
