package com.manageemployee.employeemanagement.mappers;

import com.manageemployee.employeemanagement.converter.dtoMappers.DepartmentMapper;
import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Log4j2
public class DepartmentMapperTest {

    private static final CompanyBranchService companyBranchService = Mockito.mock(CompanyBranchService.class);
    private static DepartmentMapper departmentMapper;

    private CompanyBranch companyBranchEntity;

    @BeforeAll
    static void beforeAll() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        departmentMapper = new DepartmentMapper(modelMapper, companyBranchService);
        departmentMapper.setupMapper();
    }

    @BeforeEach
    void setUp() {
        companyBranchEntity = new CompanyBranch();
        companyBranchEntity.setId(1L);
        companyBranchEntity.setCompanyBranchAddress(new Address("city", "zipCode",
                "street", "country"));
        companyBranchEntity.setBudget(new Money(
                new BigDecimal(100), Currency.getInstance("RUB")
        ));
        companyBranchEntity.setPhoneNumber("+79999999999");

    }

    @Test
    void assertThatFieldsInEntityAndDtoAreEqualsAfterConvertationFromEntityToDto() {
        Department departmentEntity = new Department();
            departmentEntity.setId(1L);
            departmentEntity.setDepartmentName("Name");
            departmentEntity.setLastModified(new Date());
            departmentEntity.setCompanyBranch(companyBranchEntity);

        DepartmentDTO dto = departmentMapper.toDto(departmentEntity);

        assertThat(dto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", departmentEntity.getId())
                .hasFieldOrPropertyWithValue("departmentName", departmentEntity.getDepartmentName())
                .hasFieldOrPropertyWithValue("lastModified", departmentEntity.getLastModified())
                .hasFieldOrPropertyWithValue("companyBranchId", departmentEntity.getCompanyBranch().getId());
    }

    @Test
    void assertThatFieldsInEntityAndDtoAreEqualsAfterConvertationFromDtoToEntity() {
        Mockito.when(companyBranchService.getCompanyBranchById(1L))
                .thenReturn(companyBranchEntity);

        DepartmentDTO dto = new DepartmentDTO();
            dto.setCompanyBranchId(1L);
            dto.setId(1L);
            dto.setDepartmentName("Name");
            dto.setLastModified(new Date());

        Department entity = departmentMapper.toEntity(dto);

        assertThat(entity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", dto.getId())
                .hasFieldOrPropertyWithValue("departmentName", dto.getDepartmentName())
                .hasFieldOrPropertyWithValue("lastModified", dto.getLastModified())
                .hasFieldOrPropertyWithValue("companyBranch", companyBranchEntity);
    }
}
