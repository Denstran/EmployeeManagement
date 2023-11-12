package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.Position;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of AbstractMapper for Position and PositionDTO
 */
@Component
public class PositionMapper extends AbstractMapper<Position, PositionDTO> {

    @Autowired
    public PositionMapper(ModelMapper mapper) {
        super(Position.class, PositionDTO.class, mapper);
    }

}
