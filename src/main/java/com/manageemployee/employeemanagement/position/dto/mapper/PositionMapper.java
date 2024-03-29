package com.manageemployee.employeemanagement.position.dto.mapper;

import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapperWithSpecificFields;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation of AbstractMapper for Position and PositionDTO
 */
@Component
public class PositionMapper extends AbstractMapperWithSpecificFields<Position, PositionDTO> {
    private final DepartmentService departmentService;

    @Autowired
    public PositionMapper(ModelMapper mapper, DepartmentService departmentService) {
        super(Position.class, PositionDTO.class, mapper);
        this.departmentService = departmentService;
    }

    /**
     * @see AbstractMapperWithSpecificFields#setupMapper()
     */
    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(Position.class, PositionDTO.class)
                .addMappings(m -> {
                    m.skip(PositionDTO::setDepartmentId);
                    m.skip(PositionDTO::setDepartmentName);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(PositionDTO.class, Position.class)
                .addMappings(m -> m.skip(Position::setDepartment)).setPostConverter(toEntityConverter());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForDto(Object, Object)
     * @param source - entity object, which will be mapped to dto
     * @param destination - dto object
     */
    @Override
    protected void mapSpecificFieldsForDto(Position source, PositionDTO destination) {
        destination.setDepartmentId(Objects.isNull(source) ||
                Objects.isNull(source.getDepartment()) ? null : source.getDepartment().getId());

        destination.setDepartmentName(source.getDepartment().getDepartmentName());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForEntity(Object, Object)
     * @param source - dto object, which will be mapped to entity
     * @param destination - entity object
     */
    @Override
    protected void mapSpecificFieldsForEntity(PositionDTO source, Position destination) {
        destination.setDepartment(Objects.isNull(source) || Objects.isNull(source.getDepartmentId()) ?
                null : departmentService.getReference(source.getDepartmentId()));
    }
}
