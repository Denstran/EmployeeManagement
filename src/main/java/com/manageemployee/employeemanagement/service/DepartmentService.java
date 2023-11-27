package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.repository.CompanyBranchRepository;
import com.manageemployee.employeemanagement.repository.DepartmentRepository;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyBranchRepository companyBranchRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository,
                             CompanyBranchRepository companyBranchRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.companyBranchRepository = companyBranchRepository;
    }

    @Transactional
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
    public void saveDepartment(Department department) {
        if (department == null)
            throw new IllegalArgumentException("Illegal department for saving!");

        departmentRepository.saveAndFlush(department);
    }

    public Department getDepartmentReferenceById(Long depId) {
        return departmentRepository.getReferenceById(depId);
    }

    @Transactional
    public void deleteDepartment(Department department) {
        if (department == null)
            throw new IllegalArgumentException("Illegal department for removal!");
        Optional<CompanyBranch> companyBranchOptional =
                companyBranchRepository.findCompanyBranchByDepartmentId(department.getId());

        if (companyBranchOptional.isEmpty())
            throw new IllegalArgumentException("Department is not bounded to any company branches!");

        CompanyBranch companyBranch = companyBranchOptional.get();
        List<Employee> employees = employeeRepository.findAllByDepartment_Id(department.getId());

        BigDecimal totalSalary = employees.stream()
                        .map(Employee::getSalary)
                                .map(Money::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        companyBranch.getBudget().setAmount(companyBranch.getBudget().getAmount().add(totalSalary));
        employeeRepository.deleteAllByDepartment(department);

        departmentRepository.delete(department);
    }
}
