package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyBranchService companyBranchService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final EmployeeStatusService employeeStatusService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, CompanyBranchService companyBranchService,
                           DepartmentService departmentService,
                           PositionService positionService, EmployeeStatusService employeeStatusService) {
        this.employeeRepository = employeeRepository;
        this.companyBranchService = companyBranchService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.employeeStatusService = employeeStatusService;
    }

    @Transactional
    public void saveEmployee(Employee employee, Long depId, Long[] selectedPositions) {
        if (employee == null)
            throw new IllegalArgumentException("Невалидный сотрудник для сохранения!");

        Department departmentRef = departmentService.getDepartmentReferenceById(depId);
        List<Position> positions = positionService.findPositionsByIds(selectedPositions);

        employee.setDepartment(departmentRef);
        employee.addPositions(positions);
        employee.setEmployeeStatus(employeeStatusService.getEmployeeStatusReferenceById(3L));
        employeeRepository.saveAndFlush(employee);
    }

    @Transactional
    public void updateEmployee(Employee employee, Long[] selectedPositions, Long[] positionsForRemoval,
                               List<Position> takenPositions) {
        if (employee == null)
            throw new IllegalArgumentException("Невалидный сотрудник для обновления!");

        employee.setPositions(new HashSet<>(takenPositions));

        if (selectedPositions != null) employee.addPositions(positionService.findPositionsByIds(selectedPositions));
        if (positionsForRemoval != null && positionsForRemoval.length > 0)
            employee.removePositions(positionService.findPositionsByIds(positionsForRemoval));

        employeeRepository.saveAndFlush(employee);
    }


    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Выбран несуществующий сотрудник"));
    }

    public List<Employee> getAllEmployeesInDepartment(Long depId) {
        if (depId == null || depId < 1)
            throw new IllegalArgumentException("Выбран несуществующий отдел!");

        return employeeRepository.findAllByDepartment_Id(depId);
    }

    @Transactional
    public void deleteEmployeeById(Long id, Long companyBranchId) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Выбран несуществующий сотрудник!"));

        CompanyBranch companyBranch = companyBranchService.getCompanyBranchById(companyBranchId);
        companyBranch.getBudget().setAmount(companyBranch.getBudget().getAmount()
                .add(employee.getSalary().getAmount()));

        companyBranchService.updateCompanyBranch(companyBranch);

        employeeRepository.deleteById(id);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty())
            return false;

        return employeeRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean existsByEmail(String email) {
        if (email == null || email.isEmpty())
            return false;

        return employeeRepository.existsByEmail(email);
    }

    public Optional<Employee> findByPhoneNumber(String phoneNumber) {
        return employeeRepository.findEmployeeByPhoneNumber(phoneNumber);
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }
}
