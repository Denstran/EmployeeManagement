package com.manageemployee.employeemanagement.mappers;


import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeMapper;
import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.dto.EmployeeStatusDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.EmployeeStatus;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.PositionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class EmployeeMapperTest {

    private static final CompanyBranchService companyBranchService = Mockito.mock(CompanyBranchService.class);
    private static final PositionService positionService = Mockito.mock(PositionService.class);
    private static EmployeeMapper employeeMapper;

    @BeforeAll
    static void beforeAll() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        employeeMapper = new EmployeeMapper(modelMapper, companyBranchService, positionService);
        employeeMapper.setupMapper();
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_entity_to_dto() {
        Name name = new Name("First name", "Middle Name", "Last Name");

        Employee employee = new Employee();
        employee.setName(name);

        EmployeeDTO dto = employeeMapper.toDto(employee);

        Assertions.assertEquals(dto.getName(), employee.getName());
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_dto_to_entity() {
        Name name = new Name("First name", "Middle Name", "Last Name");

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName(name);

        Employee entity = employeeMapper.toEntity(dto);

        Assertions.assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void assert_that_companyBranches_ids_are_equal_after_convertation_from_entity_to_dto() {
        CompanyBranch companyBranch = new CompanyBranch();
            companyBranch.setId(1L);

        Employee entity = new Employee();
        entity.setCompanyBranch(companyBranch);

        EmployeeDTO dto = employeeMapper.toDto(entity);

        Assertions.assertEquals(entity.getCompanyBranch().getId(), dto.getCompanyBranchId());
    }

    @Test
    void assert_that_companyBranches_ids_are_equal_after_convertation_from_dto_to_entity() {
        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setId(1L);

        Mockito.when(companyBranchService.getReference(1L)).thenReturn(companyBranch);

        EmployeeDTO dto  = new EmployeeDTO();
        dto.setCompanyBranchId(companyBranch.getId());
        dto.setId(1L);

        Employee entity = employeeMapper.toEntity(dto);

        Assertions.assertEquals(entity.getCompanyBranch().getId(), dto.getCompanyBranchId());
    }

    @Test
    void assert_that_employee_status_is_the_same_after_convertation_from_entity_to_dto() {
        Employee entity = new Employee();
        entity.setEmployeeStatus(new EmployeeStatus(1L, EEmployeeStatus.WORKING));

        EmployeeDTO dto = employeeMapper.toDto(entity);

        Assertions.assertEquals(entity.getEmployeeStatus().getEmployeeStatus(),
                dto.getEmployeeStatus().getEmployeeStatus());
    }

    @Test
    void assert_that_employee_status_is_the_same_after_convertation_from_dto_to_entity() {
        EmployeeDTO dto = new EmployeeDTO();
            dto.setEmployeeStatus(new EmployeeStatusDTO(1L, EEmployeeStatus.WORKING));
            dto.setId(1L);

        Employee entity = employeeMapper.toEntity(dto);

        Assertions.assertEquals(dto.getEmployeeStatus().getEmployeeStatus(),
                entity.getEmployeeStatus().getEmployeeStatus());
    }

    @Test
    void assert_that_position_id_is_the_same_after_convertation_from_entity_to_dto() {
        Position position = new Position();
            position.setId(1L);

        Employee employee = new Employee();
            employee.setPosition(position);

        EmployeeDTO dto = employeeMapper.toDto(employee);

        Assertions.assertEquals(employee.getPosition().getId(), dto.getPositionId());
    }

    @Test
    void assert_that_position_id_is_the_same_after_convertation_from_dto_to_entity() {
        Position position = new Position();
            position.setId(1L);

        Mockito.when(positionService.getReference(1L)).thenReturn(position);
        EmployeeDTO dto = new EmployeeDTO();
        dto.setPositionId(1L);

        Employee employee = employeeMapper.toEntity(dto);

        Assertions.assertEquals(employee.getPosition().getId(), dto.getPositionId());
    }

}
