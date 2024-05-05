package com.manageemployee.employeemanagement.unit.mappers;

import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.VacationRequestMapper;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.RequestStatus;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VacationRequestMapperTest {
    private static final EmployeeService employeeService = Mockito.mock(EmployeeService.class);
    private static VacationRequestMapper vacationRequestMapper;
    private static Employee employee;

    @BeforeAll
    static void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        vacationRequestMapper = new VacationRequestMapper(modelMapper, employeeService);
        vacationRequestMapper.setupMapper();
        employee = new Employee();
        employee.setId(1L);

        Mockito.when(employeeService.getReference(1L)).thenReturn(employee);
    }

    @Test
    void should_map_correctly_from_entity_to_dto() {
        VacationRequest entity = setUpEntity();
        VacationRequestDTO dto = vacationRequestMapper.toDto(entity);

        assertEquals(entity.getEmployee().getId(), dto.getEmployeeId());
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getRequestStatus(), dto.getRequestStatus());
        assertEquals(entity.getVacationStartDate(), dto.getVacationStartDate());
        assertEquals(entity.getVacationEndDate(), dto.getVacationEndDate());
    }

    @Test
    void should_map_correctly_from_dto_to_entity() {
        VacationRequestDTO dto = setUpDTO();
        VacationRequest entity = vacationRequestMapper.toEntity(dto);

        assertEquals(dto.getEmployeeId(), entity.getEmployee().getId());
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getRequestStatus(), entity.getRequestStatus());
        assertEquals(dto.getVacationStartDate(), entity.getVacationStartDate());
        assertEquals(dto.getVacationEndDate(), entity.getVacationEndDate());
    }

    private VacationRequest setUpEntity() {
        VacationRequest entity = new VacationRequest();
        entity.setId(1L);
        entity.setEmployee(employee);
        entity.setRequestStatus(RequestStatus.IN_PROCESS);
        entity.setVacationStartDate(LocalDate.now());
        entity.setVacationEndDate(LocalDate.now().plusDays(5));

        return entity;
    }

    private VacationRequestDTO setUpDTO() {
        VacationRequestDTO dto = new VacationRequestDTO();
        dto.setEmployeeId(1L);
        dto.setRequestStatus(RequestStatus.IN_PROCESS);
        dto.setVacationStartDate(LocalDate.now());
        dto.setVacationEndDate(LocalDate.now().plusDays(5));

        return dto;
    }
}
