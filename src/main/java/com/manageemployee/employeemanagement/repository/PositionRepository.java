package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Page<Position> findAllByDepartment_Id(Pageable pageable, Long departmentId);

    boolean existsByPositionNameIgnoreCaseAndDepartment_Id(String positionName, Long depId);

    Optional<Position> findByPositionNameIgnoreCaseAndDepartment_Id(String positionName, Long depId);
}
