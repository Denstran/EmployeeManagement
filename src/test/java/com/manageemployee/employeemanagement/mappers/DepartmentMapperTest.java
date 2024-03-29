package com.manageemployee.employeemanagement.mappers;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentMapper;
import com.manageemployee.employeemanagement.department.model.Department;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DepartmentMapperTest {
    private static DepartmentMapper departmentMapper;

    @BeforeAll
    static void beforeAll() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        departmentMapper = new DepartmentMapper(modelMapper);
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_entity_to_dto() {
        Department departmentEntity = new Department();
            departmentEntity.setId(1L);
            departmentEntity.setDepartmentName("Name");

        DepartmentDTO dto = departmentMapper.toDto(departmentEntity);

        assertThat(dto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", departmentEntity.getId())
                .hasFieldOrPropertyWithValue("departmentName", departmentEntity.getDepartmentName());
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_dto_to_entity() {

        DepartmentDTO dto = new DepartmentDTO();
            dto.setId(1L);
            dto.setDepartmentName("Name");

        Department entity = departmentMapper.toEntity(dto);

        assertThat(entity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", dto.getId())
                .hasFieldOrPropertyWithValue("departmentName", dto.getDepartmentName());
    }
}
