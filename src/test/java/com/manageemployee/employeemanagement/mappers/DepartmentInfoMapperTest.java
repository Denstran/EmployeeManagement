package com.manageemployee.employeemanagement.mappers;

import com.manageemployee.employeemanagement.converter.dtoMappers.DepartmentInfoMapper;
import com.manageemployee.employeemanagement.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.DepartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.math.BigDecimal;
import java.util.Currency;

public class DepartmentInfoMapperTest {
    private static final CompanyBranchService companyBranchService = Mockito.mock(CompanyBranchService.class);
    private static final DepartmentService departmentService = Mockito.mock(DepartmentService.class);
    private static DepartmentInfoMapper departmentInfoMapper;
    private static CompanyBranchDepartmentPK pk;

    @BeforeAll
    static void beforeAll() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        departmentInfoMapper = new DepartmentInfoMapper(modelMapper, companyBranchService, departmentService);
        departmentInfoMapper.setupMapper();

        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setId(1L);
        Department department = new Department();
        department.setId(1L);
        pk = new CompanyBranchDepartmentPK(companyBranch, department);
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_entity_to_dto() {
        DepartmentInfo departmentInfo = new DepartmentInfo();
            departmentInfo.setDepartmentBudget(new Money(BigDecimal.valueOf(100L),
                    Currency.getInstance("RUB")));
            departmentInfo.setAmountOfEmployee(1);
            departmentInfo.setPk(pk);

        DepartmentInfoDTO dto = departmentInfoMapper.toDto(departmentInfo);

        Assertions.assertEquals(departmentInfo.getPk().getDepartment().getId(), dto.getDepartmentId());
        Assertions.assertEquals(departmentInfo.getPk().getCompanyBranch().getId(), dto.getCompanyBranchId());
        Assertions.assertEquals(departmentInfo.getAmountOfEmployee(), dto.getAmountOfEmployee());
        Assertions.assertEquals(departmentInfo.getDepartmentBudget(), dto.getDepartmentBudget());
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_dto_to_entity() {
        DepartmentInfoDTO dto = new DepartmentInfoDTO();
            dto.setDepartmentId(1L);
            dto.setCompanyBranchId(1L);
            dto.setDepartmentBudget(new Money(BigDecimal.valueOf(100L),
                    Currency.getInstance("RUB")));
            dto.setAmountOfEmployee(1);

        CompanyBranch companyBranch = new CompanyBranch();
            companyBranch.setId(1L);
        Department department = new Department();
            department.setId(1L);

        Mockito.when(departmentService.getReference(1L)).thenReturn(department);
        Mockito.when(companyBranchService.getReference(1L)).thenReturn(companyBranch);

        DepartmentInfo departmentInfo = departmentInfoMapper.toEntity(dto);

        Assertions.assertEquals(departmentInfo.getPk().getDepartment().getId(), dto.getDepartmentId());
        Assertions.assertEquals(departmentInfo.getPk().getCompanyBranch().getId(), dto.getCompanyBranchId());
        Assertions.assertEquals(departmentInfo.getAmountOfEmployee(), dto.getAmountOfEmployee());
        Assertions.assertEquals(departmentInfo.getDepartmentBudget(), dto.getDepartmentBudget());
    }

}
