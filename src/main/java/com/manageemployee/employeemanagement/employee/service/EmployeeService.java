package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentType;
import com.manageemployee.employeemanagement.employee.dto.SearchEmployeeFilters;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import com.manageemployee.employeemanagement.employee.model.employee.Name;
import com.manageemployee.employeemanagement.employee.repository.EmployeeRepository;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.PasswordGenerator;
import com.manageemployee.employeemanagement.security.UserRole;
import com.manageemployee.employeemanagement.util.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService implements com.manageemployee.employeemanagement.department.service.EmployeeService {
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createEmployee(Employee employee) {
        Set<UserRole> roles = prepareUserRoles(employee);

        String password = passwordEncoder.encode(PasswordGenerator.generatePassword());
        employee.hireEmployee(roles, password);
        repository.saveAndFlush(employee);
    }

    private Set<UserRole> prepareUserRoles(Employee employee) {
        Set<UserRole> roles = new HashSet<>();
        if (isOnLeadingPosition(employee)) roles.add(UserRole.ROLE_HEAD_OF_DEPARTMENT);
        if (isHR(employee)) roles.add(UserRole.ROLE_HR);
        else roles.add(UserRole.ROLE_EMPLOYEE);

        return roles;
    }

    private boolean isOnLeadingPosition(Employee employee) {
        return employee.getPosition().isLeading();
    }

    private boolean isHR(Employee employee) {
        return DepartmentType.HR.equals(employee.getPosition().getDepartment().getDepartmentType());
    }

    @Transactional
    public void updateEmployee(Employee employee) {
        Employee employeeFromDB = getById(employee.getId());
        if (isRestoringEmployee(employee, employeeFromDB)) employee.restore();
        else if (isEmployeeFired(employee, employeeFromDB)) employee.fireEmployee(employeeFromDB.getSalary());
        else employee.updateEmployee(employee);

        repository.saveAndFlush(employee);
    }

    private boolean isRestoringEmployee(Employee employee, Employee employeeFromDB) {
        return EmployeeStatus.FIRED.equals(employeeFromDB.getEmployeeStatus()) &&
                !EmployeeStatus.FIRED.equals(employee.getEmployeeStatus());
    }

    private boolean isEmployeeFired(Employee employee, Employee employeeFromDB) {
        return employee.getEmployeeStatus().equals(EmployeeStatus.FIRED)
                && !employeeFromDB.getEmployeeStatus().equals(EmployeeStatus.FIRED);
    }

    @Transactional
    public void deleteAllByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department) {
        repository.deleteEmployeesByCompanyBranchAndDepartment(companyBranch, department);
    }

    public Employee getReference(Long id) {
        return repository.getReferenceById(id);
    }

    public String getEmployeeNameById(Long employeeId) {
        Optional<Name> employeeName = repository.getEmployeeNameById(employeeId);
        return employeeName.map(Name::toString).orElseThrow(()
                -> new IllegalArgumentException("Выбранного сотрудника не существует"));
    }

    public DepartmentInfo getEmployeeDepartmentInfo(Long companyBranchId, Long employeePositionId) {
        return repository.findEmployeeDepartmentInfo(companyBranchId, employeePositionId).orElseThrow(() ->
                new IllegalArgumentException("Не удалось найти отдел по входным данным!"));
    }

    public Employee getById(Long employeeId) {
        return repository.findById(employeeId).orElseThrow(() ->
                new IllegalArgumentException("Выбранного сотрудника не существует!"));
    }

    public Optional<Employee> getByEmail(String email) {
        return repository.findEmployeeByEmail(email);
    }

    public Optional<Employee> getByPhoneNumber(String phoneNumber) {
        return repository.findEmployeeByPhoneNumber(phoneNumber);
    }

    public Money countEmployeeSalariesByCompanyBranchAndDepartment(CompanyBranch companyBranch, Department department) {
        List<Employee> employees =
                repository.findWorkingEmployeesByCompanyBranchAndDepartment(companyBranch, department);

        return countEmployeesTotalSalary(employees);
    }

    private Money countEmployeesTotalSalary(List<Employee> employees) {
        Money totalResult = new Money(0.0);
        for (Employee employee : employees)
            totalResult = Money.sum(totalResult, employee.getSalary());
        return totalResult;
    }

    public boolean existsByPositionAndCompanyBranch(Position position, Long companyBranchId) {
        return repository.existsByPositionAndCompanyBranch_Id(position, companyBranchId);
    }

    public Optional<Employee> getByPositionAndCompanyBranchId(Position position, Long companyBranchId) {
        return repository.findByPositionAndCompanyBranch_Id(position, companyBranchId);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

    public List<Employee> getAllEmployee(SearchEmployeeFilters filters) {
        Specification<Employee> spec = EmployeeSpecification.setupSpecification(filters.getCompanyBranchId(),
                filters.getDepartmentId());

        spec = processEmailFilter(filters.getEmail(), spec);
        spec = processPositionFilter(filters.getPositionName(), spec);
        spec = processPhoneNumberFilter(filters.getPhoneNumber(), spec);
        spec = processStatusFilter(filters.getEmployeeStatus(), spec);
        spec = processSalaryFilter(filters.getSalaryStart(), filters.getSalaryEnd(), spec);
        spec = processWorkingYearsFilter
                (filters.getStartAmountOfWorkingYears(), filters.getEndAmountOfWorkingYears(), spec);

        return repository.findAll(spec);
    }

    private Specification<Employee> processEmailFilter(String email, Specification<Employee> spec) {
        if (isStringFilterOn(email)) return spec.and(EmployeeSpecification.isEqualToEmail(email));

        return spec;
    }

    private Specification<Employee> processPositionFilter(String positionName, Specification<Employee> spec) {
        if (isStringFilterOn(positionName)) return spec.and(EmployeeSpecification.isEqualToPosition(positionName));

        return spec;
    }

    private Specification<Employee> processPhoneNumberFilter(String phoneNumber, Specification<Employee> spec) {
        if (isStringFilterOn(phoneNumber)) return spec.and(EmployeeSpecification.isEqualToPhoneNumber(phoneNumber));

        return spec;
    }

    private Specification<Employee> processStatusFilter(String status, Specification<Employee> spec) {
        if (isStringFilterOn(status))
            return spec.and(EmployeeSpecification.isEqualToStatus(EmployeeStatus.valueOf(status)));

        return spec;
    }

    private Specification<Employee> processSalaryFilter(Double startSalary, Double endSalary,
                                                        Specification<Employee> spec) {
        if (isNotNullNumberFilter(startSalary, endSalary))
            return spec.and(EmployeeSpecification.isSalaryBetween(startSalary, endSalary));

        return spec;
    }

    private Specification<Employee> processWorkingYearsFilter(Integer start, Integer end, Specification<Employee> spec) {
        if (isNotNullNumberFilter(start, end)) return spec.and(EmployeeSpecification.isInWorkingYearsRange(start, end));

        return spec;
    }

    private boolean isNotNullNumberFilter(Number... numbers) {
        for (Number number : numbers)
            if (number == null) return false;

        return true;
    }

    private boolean isStringFilterOn(String filter) {
        return filter != null && !filter.isEmpty() && !filter.equals("ALL");
    }

    public Employee getDepartmentBoss(CompanyBranch companyBranch, Department department) {
        return repository.findDepartmentBoss(companyBranch, department);
    }

}
