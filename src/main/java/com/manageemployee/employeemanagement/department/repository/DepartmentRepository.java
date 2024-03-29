package com.manageemployee.employeemanagement.department.repository;

import com.manageemployee.employeemanagement.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(String depName);
    Optional<Department> findDepartmentByDepartmentName(String depName);

    @Query("SELECT d FROM Department d WHERE d.id = (SELECT p.department.id FROM Position p WHERE p.id = :positionId)")
    Optional<Department> findDepartmentByPositionId(@Param("positionId") Long positionId);

    @Query("SELECT dep FROM Department dep WHERE dep.id NOT IN " +
            "(SELECT di.pk.department.id FROM DepartmentInfo di WHERE di.pk.companyBranch.id = :cmpId)")
    List<Department> getAvailableDepartments(@Param("cmpId") Long companyBranchId);

    @Query("SELECT dep FROM Department dep " +
            "WHERE dep.id = :depId " +
            "OR dep.id NOT IN (SELECT di.pk.department.id FROM DepartmentInfo di WHERE di.pk.companyBranch.id = :cmpId)")
    List<Department> getAvailableDepartmentsWhenUpdating(@Param("cmpId") Long companyBranchId,
                                                        @Param("depId") Long departmentRepository);

    @Query("SELECT d FROM Department d WHERE d = " +
            "(SELECT p.department FROM Position p WHERE p.positionName = :positionName)")
    Department findByPositionName(@Param("positionName") String positionName);
}
