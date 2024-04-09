package com.manageemployee.employeemanagement.unit.mappers;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentInfoMapper;
import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.util.Money;
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
        department.setDepartmentName("HR");
        department.setId(1L);
        pk = new CompanyBranchDepartmentPK(companyBranch, department);
    }

    @Test
    void assert_that_fields_are_equal_after_convertation_from_entity_to_dto() {
        DepartmentInfo departmentInfo = new DepartmentInfo();
            departmentInfo.setDepartmentBudget(new Money(BigDecimal.valueOf(100L),
                    Currency.getInstance("RUB")));
            departmentInfo.setPk(pk);

        DepartmentInfoDTO dto = departmentInfoMapper.toDto(departmentInfo);

        Assertions.assertEquals(departmentInfo.getPk().getDepartment().getId(), dto.getDepartmentId());
        Assertions.assertEquals(departmentInfo.getPk().getCompanyBranch().getId(), dto.getCompanyBranchId());
        Assertions.assertEquals(departmentInfo.getDepartmentBudget(), dto.getDepartmentBudget());
    }

    @Test
    void assert_that_department_name_is_correct_after_convertation_from_entity_to_dto() {
        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setPk(pk);

        DepartmentInfoDTO dto = departmentInfoMapper.toDto(departmentInfo);

        Assertions.assertEquals("HR", dto.getDepartmentName());
    }

    @Test
    void assert_that_convertion_is_successful_when_dto_has_department_name() {
        DepartmentInfoDTO dto = new DepartmentInfoDTO();
            dto.setDepartmentId(pk.getDepartment().getId());
            dto.setCompanyBranchId(pk.getCompanyBranch().getId());
            dto.setDepartmentName("HR");

        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setId(1L);
        Department department = new Department();
        department.setId(1L);

        Mockito.when(departmentService.getReference(1L)).thenReturn(department);
        Mockito.when(companyBranchService.getReference(1L)).thenReturn(companyBranch);

        DepartmentInfo departmentInfo = departmentInfoMapper.toEntity(dto);
        Assertions.assertEquals(departmentInfo.getPk(), pk);
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
        Assertions.assertEquals(departmentInfo.getDepartmentBudget(), dto.getDepartmentBudget());
    }

}
