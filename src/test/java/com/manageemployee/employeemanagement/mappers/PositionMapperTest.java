package com.manageemployee.employeemanagement.mappers;

import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.DepartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class PositionMapperTest {
    private static final DepartmentService departmentService = Mockito.mock(DepartmentService.class);
    private static PositionMapper positionMapper;

    @BeforeAll
    static void beforeAll() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        positionMapper = new PositionMapper(modelMapper, departmentService);
        positionMapper.setupMapper();
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_entity_to_dto() {
        Position position = new Position();
            position.setId(1L);
            position.setPositionName("Test");
            position.setAmountOfEmployees(1);

        PositionDTO dto = positionMapper.toDto(position);

        Assertions.assertEquals(position.getId(), dto.getId());
        Assertions.assertEquals(position.getPositionName(), dto.getPositionName());
        Assertions.assertEquals(position.getAmountOfEmployees(), dto.getAmountOfEmployees());
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_dto_to_entity() {
        PositionDTO dto = new PositionDTO();
            dto.setId(1L);
            dto.setPositionName("Test");
            dto.setAmountOfEmployees(1);

        Position position = positionMapper.toEntity(dto);

        Assertions.assertEquals(position.getId(), dto.getId());
        Assertions.assertEquals(position.getPositionName(), dto.getPositionName());
        Assertions.assertEquals(position.getAmountOfEmployees(), dto.getAmountOfEmployees());
    }

    @Test
    void assert_that_department_ids_are_equal_after_convertation_from_dto_to_entity() {
        Department department = new Department();
            department.setId(1L);
        PositionDTO dto = new PositionDTO();
            dto.setDepartmentId(1L);

        Mockito.when(departmentService.getReference(1L)).thenReturn(department);
        Position position = positionMapper.toEntity(dto);

        Assertions.assertEquals(position.getDepartment().getId(), dto.getDepartmentId());
    }

    @Test
    void assert_that_department_ids_are_equal_after_convertation_from_entity_to_dto() {
        Department department = new Department();
            department.setId(1L);

        Position position = new Position();
            position.setDepartment(department);

        PositionDTO dto = positionMapper.toDto(position);

        Assertions.assertEquals(position.getDepartment().getId(), dto.getDepartmentId());
    }
}
