package com.manageemployee.employeemanagement.employee.dto.mapper;

import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.model.Employee;
import com.manageemployee.employeemanagement.position.service.PositionService;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserService;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapperWithSpecificFields;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * Implementation of AbstractMapperWithSpecificFields for Employee and EmployeeDTO
 */
@Component
@Slf4j
public class EmployeeMapper extends AbstractMapperWithSpecificFields<Employee, EmployeeDTO> {
    private final CompanyBranchService companyBranchService;
    private final PositionService positionService;
    private final UserDetailsService userService;

    @Autowired
    public EmployeeMapper(ModelMapper mapper, CompanyBranchService companyBranchService,
                          PositionService positionService, UserService userService) {
        super(Employee.class, EmployeeDTO.class, mapper);
        this.companyBranchService = companyBranchService;
        this.positionService = positionService;
        this.userService = userService;
    }

    /**
     * Set up method for creating specific type maps for model mapper for mapping specific fields
     */
    @PostConstruct
    @Override
    public void setupMapper() {
        mapper.createTypeMap(Employee.class, EmployeeDTO.class)
                .addMappings(m -> {
                    m.skip(EmployeeDTO::setCompanyBranchId);
                    m.skip(EmployeeDTO::setPositionId);
                    m.skip(EmployeeDTO::setPositionName);
                    m.skip(EmployeeDTO::setDepartmentName);
                    m.skip(EmployeeDTO::setYearsOfWorking);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(EmployeeDTO.class, Employee.class)
                .addMappings(m -> {
                    m.skip(Employee::setCompanyBranch);
                    m.skip(Employee::setPosition);
                    m.skip(Employee::setUser);
                }).setPostConverter(toEntityConverter());
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForDto(Object, Object)
     * @param source - entity object, which will be mapped to dto
     * @param destination - dto object
     */
    @Override
    protected void mapSpecificFieldsForDto(Employee source, EmployeeDTO destination) {
        log.info("FROM EMPLOYEE_MAPPER: MAPPING {} TO DTO", source);
        destination.setCompanyBranchId(Objects.isNull(source) ||
                Objects.isNull(source.getCompanyBranch()) ? null : source.getCompanyBranch().getId());

        destination.setPositionId(Objects.isNull(source) ||
                Objects.isNull(source.getPosition()) ? null : source.getPosition().getId());

        destination.setPositionName(source.getPosition().getPositionName());
        destination.setDepartmentName(source.getPosition().getDepartment().getDepartmentName());
        int yearsOfWorking = new Date().getYear() - source.getEmploymentDate().getYear();
        destination.setYearsOfWorking(yearsOfWorking);
        log.info("FROM EMPLOYEE_MAPPER: MAPPING END, RESULT: {}", destination);
    }

    /**
     * @see AbstractMapperWithSpecificFields#mapSpecificFieldsForEntity(Object, Object)
     * @param source - dto object, which will be mapped to entity
     * @param destination - entity object
     */
    @Override
    protected void mapSpecificFieldsForEntity(EmployeeDTO source, Employee destination) {
        destination.setCompanyBranch(Objects.isNull(source.getCompanyBranchId()) ? null :
                companyBranchService.getReference(source.getCompanyBranchId()));

        destination.setPosition(Objects.isNull(source.getPositionId()) ? null :
                positionService.getReference(source.getPositionId()));
        try {
            User user = (User) userService.loadUserByUsername(source.getEmail());
            destination.setUser(user);
        }catch (UsernameNotFoundException e) {
            destination.setUser(null);
        }
    }
}
