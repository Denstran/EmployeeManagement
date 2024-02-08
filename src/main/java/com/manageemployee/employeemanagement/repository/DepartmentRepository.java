package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(String depName);
    Optional<Department> findDepartmentByDepartmentName(String depName);

    @Query("SELECT dep FROM Department dep WHERE dep.id NOT IN " +
            "(SELECT di.pk.department.id FROM DepartmentInfo di WHERE di.pk.companyBranch.id = :cmpId)")
    List<Department> getAvailableDepartments(@Param("cmpId") Long companyBranchId);

    @Query("SELECT dep FROM Department dep " +
            "WHERE dep.id = :depId " +
            "OR dep.id NOT IN (SELECT di.pk.department.id FROM DepartmentInfo di WHERE di.pk.companyBranch.id = :cmpId)")
    List<Department> getAvailableDepartmentsWhenUpdating(@Param("cmpId") Long companyBranchId,
                                                        @Param("depId") Long departmentRepository);
}
