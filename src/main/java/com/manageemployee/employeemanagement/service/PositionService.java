package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {
    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Transactional
    public Position createPosition(Position position) {
        return positionRepository.saveAndFlush(position);
    }

    @Transactional
    public Position updatePosition(Position position) {
        return positionRepository.saveAndFlush(position);
    }

    public Position getPositionById(Long id) {
        return positionRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Выбрана не существующая должность!"));
    }

    public Page<Position> getPositionsByDepartment(Pageable pageable, Long departmentId) {
        return positionRepository.findAllByDepartment_Id(pageable, departmentId);
    }

    public boolean existsByNameAndDepartmentId(String positionName, Long depId) {
        return positionRepository.existsByPositionNameIgnoreCaseAndDepartment_Id(positionName, depId);
    }

    public Optional<Position> findByNameAndDepartmentId(String positionName, Long depId) {
        return positionRepository.findByPositionNameIgnoreCaseAndDepartment_Id(positionName, depId);
    }

    public void deletePositionById(Long id) {
        Position position = getPositionById(id);
        if (position.getAmountOfEmployees() != 0)
            throw new UnsupportedOperationException("Нельзя удалить должность на которой числятся сотрудники!");

        positionRepository.deleteById(id);
    }
}
