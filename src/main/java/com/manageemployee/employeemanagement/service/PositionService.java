package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Position getReference(Long positionId) {
        if (positionId == null || positionId <= 0)
            throw new IllegalArgumentException("Выбрана не существующая должность!");

        return positionRepository.getReferenceById(positionId);
    }

    public List<Position> getPositionsByDepartmentId(Long departmentId) {
        return positionRepository.getPositionsByDepartmentId(departmentId);
    }
}
