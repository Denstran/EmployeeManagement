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

    public Position createOrUpdatePosition(Position position) {
        return positionRepository.saveAndFlush(position);
    }

    public Position getPositionById(Long id) {
        return positionRepository.findById(id).orElse(null);
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public void deletePositionById(Long id) {
        positionRepository.deleteById(id);
    }
}
