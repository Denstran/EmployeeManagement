package com.manageemployee.employeemanagement.validators;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.util.validators.companyBranchValidators.CompanyBranchNewEntryValidator;
import com.manageemployee.employeemanagement.util.validators.companyBranchValidators.CompanyBranchUpdatingEntryValidation;
import com.manageemployee.employeemanagement.util.validators.companyBranchValidators.CompanyBranchValidator;
import com.manageemployee.employeemanagement.util.validators.markers.CompanyBranchDTOValidator;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.List;
import java.util.Optional;

public class CompanyBranchValidatorTest {
    private static final CompanyBranchService companyBranchService = Mockito.mock(CompanyBranchService.class);
    private static CompanyBranchValidator companyBranchValidator;
    private BeanPropertyBindingResult bindingResult;
    private CompanyBranchDTO dto;

    @BeforeAll
    public static void setUp() {
        CompanyBranchNewEntryValidator newEntryValidator = new CompanyBranchNewEntryValidator(companyBranchService);
        CompanyBranchUpdatingEntryValidation updatingEntryValidation =
                new CompanyBranchUpdatingEntryValidation(companyBranchService);

        List<CompanyBranchDTOValidator> validators = List.of(newEntryValidator, updatingEntryValidation);

        companyBranchValidator = new CompanyBranchValidator(validators);
    }

    @BeforeEach
    public void beforeEach() {
        dto = new CompanyBranchDTO();
        bindingResult = new BeanPropertyBindingResult(dto, "dto");
    }

    @AfterEach
    public void afterEach() {
        dto.setPhoneNumber(null);
        dto.setPhoneNumber(null);
    }

    @Test
    public void assert_that_cant_create_company_branch_with_the_existing_phone_number() {
        dto.setPhoneNumber("1111");
        Mockito.when(companyBranchService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(true);
        companyBranchValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasErrors());
    }

    @Test
    public void assert_that_cant_create_company_branch_with_the_existing_address() {
        dto.setCompanyBranchAddress(new Address("city", "zipCode",
                "street", 2, "country"));
        Mockito.when(companyBranchService.existsByAddress(dto.getCompanyBranchAddress())).thenReturn(true);
        companyBranchValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasErrors());
    }

    @Test
    public void assert_that_cant_change_company_branch_phone_number_to_the_existing_one() {
        CompanyBranch cb = new CompanyBranch();
            cb.setId(1L);
            cb.setPhoneNumber("1111");
        Optional<CompanyBranch> companyBranch = Optional.of(cb);
        dto.setPhoneNumber("1111");
        dto.setId(2L);

        Mockito.when(companyBranchService.findByPhoneNumber(dto.getPhoneNumber())).thenReturn(companyBranch);

        companyBranchValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasErrors());
    }

    @Test
    public void assert_that_cant_change_company_branch_address_to_the_existing_one() {
        CompanyBranch cb = new CompanyBranch();
            cb.setId(1L);
            cb.setCompanyBranchAddress(new Address("city", "zipCode",
                    "street", 2, "country"));
        Optional<CompanyBranch> companyBranch = Optional.of(cb);

        dto.setCompanyBranchAddress(new Address("city", "zipCode",
                "street", 2, "country"));
        dto.setId(2L);

        Mockito.when(companyBranchService.findByAddress(dto.getCompanyBranchAddress())).thenReturn(companyBranch);

        companyBranchValidator.validate(dto, bindingResult);
        Assertions.assertTrue(bindingResult.hasErrors());
    }

    @Test
    public void assert_that_can_create_company_branch_with_the_new_phone_number() {
        dto.setPhoneNumber("1111");
        Mockito.when(companyBranchService.existsByPhoneNumber(dto.getPhoneNumber())).thenReturn(false);
        companyBranchValidator.validate(dto, bindingResult);

        Assertions.assertFalse(bindingResult.hasErrors());
    }

    @Test
    public void assert_that_can_create_company_branch_with_the_new_address() {
        dto.setCompanyBranchAddress(new Address("city", "zipCode",
                "street", 2, "country"));
        Mockito.when(companyBranchService.existsByAddress(dto.getCompanyBranchAddress())).thenReturn(false);
        companyBranchValidator.validate(dto, bindingResult);

        Assertions.assertFalse(bindingResult.hasErrors());
    }

    @Test
    public void assert_that_can_update_data_with_own_address() {
        CompanyBranch cb = new CompanyBranch();
            cb.setId(1L);
            cb.setCompanyBranchAddress(new Address("city", "zipCode",
                    "street", 2, "country"));
        Optional<CompanyBranch> companyBranch = Optional.of(cb);
        dto.setCompanyBranchAddress(new Address("city", "zipCode",
                "street", 2, "country"));
        dto.setId(1L);

        Mockito.when(companyBranchService.findByAddress(dto.getCompanyBranchAddress())).thenReturn(companyBranch);

        companyBranchValidator.validate(dto, bindingResult);
        Assertions.assertFalse(bindingResult.hasErrors());
    }

    @Test
    public void assert_that_can_update_data_with_own_phone_number() {
        CompanyBranch cb = new CompanyBranch();
            cb.setId(1L);
            cb.setPhoneNumber("1111");
        Optional<CompanyBranch> companyBranch = Optional.of(cb);
        dto.setPhoneNumber("1111");
        dto.setId(1L);

        Mockito.when(companyBranchService.findByPhoneNumber(dto.getPhoneNumber())).thenReturn(companyBranch);

        companyBranchValidator.validate(dto, bindingResult);

        Assertions.assertFalse(bindingResult.hasErrors());
    }
}
