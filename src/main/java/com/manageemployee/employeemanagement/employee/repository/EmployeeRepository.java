package com.manageemployee.employeemanagement.employee.repository;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.employee.model.Name;
import com.manageemployee.employeemanagement.position.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @Query("SELECT e FROM Employee e WHERE e.companyBranch.id = :companyBranchId " +
            "AND e.position.department.id = :departmentId")
    List<Employee> getEmployeeByCompanyBranchAndDepartment(@Param("companyBranchId") Long companyBranchId,
                                                           @Param("departmentId") Long departmentId);

    @Query("SELECT e.name FROM Employee e WHERE e.id = :employeeId")
    Optional<Name> getEmployeeNameById(@Param("employeeId") Long employeeId);

    @Query("SELECT di FROM DepartmentInfo di WHERE di.pk.companyBranch.id = :companyBranchId AND " +
            "di.pk.department.id = (SELECT p.department.id FROM Position p WHERE p.id = :positionId)")
    Optional<DepartmentInfo> findEmployeeDepartmentInfo(@Param("companyBranchId") Long companyBranchId,
                                                        @Param("positionId") Long positionId);

    @Query("SELECT e FROM Employee e WHERE e.companyBranch = :companyBranch " +
            "AND e.position.department = :department AND e.employeeStatus != 'FIRED'")
    List<Employee> findWorkingEmployeesByCompanyBranchAndDepartment(@Param("companyBranch") CompanyBranch companyBranch,
                                                                    @Param("department") Department department);

    @Query("UPDATE Employee e SET e.employeeStatus = 'FIRED' WHERE e.companyBranch=:companyBranch " +
            "AND e.position IN (SELECT p FROM Position p WHERE p.department=:department)")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteEmployeesByCompanyBranchAndDepartment(@Param("companyBranch") CompanyBranch companyBranch,
                                                     @Param("department") Department department);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    Optional<Employee> findEmployeeByPhoneNumber(String phoneNumber);
    Optional<Employee> findEmployeeByEmail(String email);

    boolean existsByPositionAndCompanyBranch_Id(Position position, Long companyBranchId);

    Optional<Employee> findByPositionAndCompanyBranch_Id(Position position, Long companyBranchId);
}
