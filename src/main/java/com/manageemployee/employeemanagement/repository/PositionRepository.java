package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> getPositionsByDepartmentId(Long departmentId);

    boolean existsByPositionNameIgnoreCaseAndDepartment_Id(String positionName, Long depId);

    Optional<Position> getByPositionNameIgnoreCaseAndDepartment_Id(String positionName, Long depId);
}
