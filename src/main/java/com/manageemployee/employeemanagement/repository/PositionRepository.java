package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Page<Position> findAllByDepartment_Id(Pageable pageable, Long departmentId);

    boolean existsByPositionNameIgnoreCaseAndDepartment_Id(String positionName, Long depId);

    Optional<Position> findByPositionNameIgnoreCaseAndDepartment_Id(String positionName, Long depId);

    @Query("SELECT p FROM Position p WHERE p.department.id = :depId AND p.requiredEmployeeAmount > 0")
    List<Position> findAvailablePositionsByDepartment(@Param("depId") Long depId);

    @Query("SELECT p FROM Position p WHERE p NOT IN (SELECT e.positions FROM Employee e WHERE e.id = :empId) AND p.department.id = :depId")
    List<Position> findAvailablePositionsByDepartmentExceptEmployee(@Param("depId") Long depId,
                                                                    @Param("empId") Long empId);

    @Query("SELECT p FROM Position p WHERE p IN (SELECT e.positions FROM Employee e WHERE e.id = :empId)")
    List<Position> findPositionsByEmployeeId(@Param("empId") Long empId);
}
