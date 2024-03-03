package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.companyBranch.id = :companyBranchId " +
            "AND e.position.department.id = :departmentId")
    List<Employee> getEmployeeByCompanyBranchAndDepartment(@Param("companyBranchId") Long companyBranchId,
                                                           @Param("departmentId") Long departmentId);

    @Query("SELECT e.name FROM Employee e WHERE e.id = :employeeId")
    Optional<Name> getEmployeeNameById(@Param("employeeId") Long employeeId);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    @Query("SELECT di FROM DepartmentInfo di WHERE di.pk.companyBranch.id = :companyBranchId AND " +
            "di.pk.department.id = (SELECT p.department.id FROM Position p WHERE p.id = :positionId)")
    Optional<DepartmentInfo> findEmployeeDepartmentInfo(@Param("companyBranchId") Long companyBranchId,
                                                        @Param("positionId") Long positionId);
    Optional<Employee> findEmployeeByPhoneNumber(String phoneNumber);
    Optional<Employee> findEmployeeByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.companyBranch = :companyBranch " +
            "AND e.position.department = :department AND e.employeeStatus.employeeStatus != 'FIRED'")
    List<Employee> findWorkingEmployeesByCompanyBranchAndDepartment(@Param("companyBranch") CompanyBranch companyBranch,
                                                                    @Param("department") Department department);

    void deleteAllByCompanyBranchAndPosition_Department(CompanyBranch companyBranch, Department department);
    void deleteAllByCompanyBranch(CompanyBranch companyBranch);
}
