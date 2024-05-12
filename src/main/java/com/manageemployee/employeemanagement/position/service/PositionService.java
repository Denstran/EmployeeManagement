package com.manageemployee.employeemanagement.position.service;

import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.position.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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

    public Position getReference(Long positionId) {
        if (positionId == null || positionId <= 0)
            throw new IllegalArgumentException("Выбрана не существующая должность!");

        return positionRepository.getReferenceById(positionId);
    }

    @Transactional
    public void savePosition(Position position) {
        positionRepository.saveAndFlush(position);
    }

    public List<Position> getAllPositions(Department department, String isLeading) {
        Specification<Position> spec = Specification.where(null);

        if (isLeading != null && !isLeading.isEmpty() && !isLeading.equals("all")) {
            boolean leading = isLeading.equalsIgnoreCase("true");
            spec = spec.and(PositionSpec.isLeading(leading));
        }

        if (department != null)
            spec = spec.and(PositionSpec.isEqualToDepartment(department));


        return positionRepository.findAll(spec);
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Optional<Position> getByName(String positionName) {
        return positionRepository.findByPositionNameIgnoreCase(positionName);
    }

    public Optional<Position> getByLeadingAndDepartmentId(boolean isLeading, Long departmentId) {
        return positionRepository.findByLeadingAndDepartment_Id(isLeading, departmentId);
    }

    public boolean existsByLeadingAndDepartmentId(boolean isLeading, Long departmentId) {
        return positionRepository.existsByLeadingAndDepartment_Id(isLeading, departmentId);
    }

    public boolean existsByPositionName(String positionName) {
        return positionRepository.existsByPositionNameIgnoreCase(positionName);
    }

    public List<Position> getPositionsByDepartmentId(Long departmentId) {
        return positionRepository.getPositionsByDepartmentId(departmentId);
    }

    public Position getById(Long positionId) {
        return positionRepository.findById(positionId).orElseThrow(()
                -> new IllegalArgumentException("Выбранной должности не существует!"));
    }
}
