package com.manageemployee.employeemanagement.mappers;


import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeMapper;
import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.dto.EmployeeStatusDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.EmployeeStatus;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.service.DepartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Date;

public class EmployeeMapperTest {

    private static final DepartmentService departmentService = Mockito.mock(DepartmentService.class);
    private static EmployeeMapper employeeMapper;

    @BeforeAll
    static void beforeAll() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        employeeMapper = new EmployeeMapper(modelMapper, departmentService);
        employeeMapper.setupMapper();
    }

    @Test
    void assertThatFieldsInEntityAndDtoAreEqualsAfterConvertationFromEntityToDto() {
        Name name = new Name("First name", "Middle Name", "Last Name");

        Employee employee = new Employee();
        employee.setName(name);

        EmployeeDTO dto = employeeMapper.toDto(employee);

        Assertions.assertEquals(dto.getName(), employee.getName());
    }

    @Test
    void assertThatFieldsInEntityAndDtoAreEqualsAfterConvertationFromDtoToEntity() {
        Name name = new Name("First name", "Middle Name", "Last Name");

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName(name);

        Employee entity = employeeMapper.toEntity(dto);

        Assertions.assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void assertThatDepartmentIdIsTheSameAfterConvertationFromEntityToDto() {
        Department departmentEntity = new Department();
        departmentEntity.setId(1L);
        departmentEntity.setDepartmentName("Name");
        departmentEntity.setLastModified(new Date());

        Employee entity = new Employee();
        entity.setDepartment(departmentEntity);

        EmployeeDTO dto = employeeMapper.toDto(entity);

        Assertions.assertEquals(entity.getDepartment().getId(), dto.getDepartmentId());
    }

    @Test
    void assertThatEmployeeStatusHasTheSameValueAfterConvertationFromEntityToDto() {
        Employee entity = new Employee();
        entity.setEmployeeStatus(new EmployeeStatus(1L, EEmployeeStatus.WORKING));

        EmployeeDTO dto = employeeMapper.toDto(entity);

        Assertions.assertEquals(entity.getEmployeeStatus().getEmployeeStatus(),
                dto.getEmployeeStatus().getEmployeeStatus());
    }

    @Test
    void assertThatDepartmentIdIsTheSameAfterConvertationFromDtoToEntity() {
        Department departmentEntity = new Department();
        departmentEntity.setId(1L);
        departmentEntity.setDepartmentName("Name");
        departmentEntity.setLastModified(new Date());

        Mockito.when(departmentService.getDepartmentById(1L)).thenReturn(departmentEntity);

        EmployeeDTO dto  = new EmployeeDTO();
            dto.setDepartmentId(departmentEntity.getId());
            dto.setId(1L);

        Employee entity = employeeMapper.toEntity(dto);

        Assertions.assertEquals(entity.getDepartment().getId(), dto.getDepartmentId());
    }

    @Test
    void assertThatEmployeeStatusHasTheSameValueAfterConvertationFromDtoToEntity() {
        EmployeeDTO dto = new EmployeeDTO();
            dto.setEmployeeStatus(new EmployeeStatusDTO(1L, EEmployeeStatus.WORKING));
            dto.setId(1L);

        Employee entity = employeeMapper.toEntity(dto);

        Assertions.assertEquals(dto.getEmployeeStatus().getEmployeeStatus(),
                entity.getEmployeeStatus().getEmployeeStatus());
    }

    @Test
    void assertThatEmployeeDtoWithPositionIdMappingCorrectlyToEmployeeEntity() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(1L);
            employeeDTO.setPositionId(1L);

        Employee entity = employeeMapper.toEntity(employeeDTO);

        Assertions.assertEquals(employeeDTO.getId(), entity.getId());
    }
}
