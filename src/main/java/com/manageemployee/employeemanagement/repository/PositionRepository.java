package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
