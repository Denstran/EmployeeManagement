package com.manageemployee.employeemanagement.mappers;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.companyBranch.dto.mappers.CompanyBranchMapper;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.util.Address;
import com.manageemployee.employeemanagement.util.Money;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Log4j2
public class CompanyBranchMapperTest {

    private static ModelMapper mapper;
    private CompanyBranchMapper companyBranchMapper;

    @BeforeAll
    public static void setUp() {
        mapper = new ModelMapper();

        mapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
    }

    @BeforeEach
    public void setUpBeforeTest(){
        companyBranchMapper = new CompanyBranchMapper(mapper);
    }

    @Test
    public void assert_that_fields_are_equal_after_convertation_from_entity_to_dto() {
        CompanyBranch entity = new CompanyBranch();
            entity.setId(1L);
            entity.setCompanyBranchAddress(new Address("city", "zipCode",
                    "street", 2, "country"));
            entity.setBudget(new Money(
                    new BigDecimal(100), Currency.getInstance("RUB")
            ));
            entity.setPhoneNumber("+79999999999");

        CompanyBranchDTO dto = companyBranchMapper.toDto(entity);

        assertThat(dto).usingRecursiveComparison().isEqualTo(entity);
    }

    @Test
    public void assert_that_fields_are_equal_after_convertation_from_dto_to_entity() {
        CompanyBranchDTO dto = new CompanyBranchDTO();
        dto.setId(1L);
        dto.setCompanyBranchAddress(new Address("city", "zipCode",
                    "street", 1, "country"));
        dto.setBudget(new Money(
                    new BigDecimal(100), Currency.getInstance("RUB")
            ));
        dto.setPhoneNumber("+79999999999");

        CompanyBranch entity = companyBranchMapper.toEntity(dto);

        assertThat(entity).hasFieldOrPropertyWithValue("phoneNumber", "+79999999999");
        assertThat(entity).hasFieldOrPropertyWithValue("id", 1L);
    }
}
