package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
}
