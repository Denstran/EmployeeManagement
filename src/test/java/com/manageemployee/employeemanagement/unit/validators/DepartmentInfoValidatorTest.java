package com.manageemployee.employeemanagement.unit.validators;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.department.validation.departmentInfoValidation.DepartmentInfoNewEntryValidator;
import com.manageemployee.employeemanagement.department.validation.departmentInfoValidation.DepartmentInfoUpdatingEntryValidator;
import com.manageemployee.employeemanagement.department.validation.departmentInfoValidation.DepartmentInfoValidator;
import com.manageemployee.employeemanagement.util.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.List;

public class DepartmentInfoValidatorTest {
    private static final CompanyBranchService companyBranchService = Mockito.mock(CompanyBranchService.class);
    private static final DepartmentInfoService departmentInfoService = Mockito.mock(DepartmentInfoService.class);
    private static final DepartmentService departmentService = Mockito.mock(DepartmentService.class);
    private static DepartmentInfoValidator departmentInfoValidator;
    private BeanPropertyBindingResult bindingResult;
    private DepartmentInfoDTO dto;
    private DepartmentInfo departmentInfo;
    private static CompanyBranch companyBranch;
    private static Department department;
    private static CompanyBranchDepartmentPK pk;

    @BeforeAll
    static void beforeAll() {
        DepartmentInfoNewEntryValidator newEntryValidator = new DepartmentInfoNewEntryValidator(
                companyBranchService, departmentService, departmentInfoService
        );

        DepartmentInfoUpdatingEntryValidator updatingEntryValidator = new DepartmentInfoUpdatingEntryValidator(
               departmentInfoService, companyBranchService, departmentService
        );

        departmentInfoValidator = new DepartmentInfoValidator(List.of(newEntryValidator, updatingEntryValidator));

        companyBranch = new CompanyBranch();
        companyBranch.setId(1L);
        department = new Department();
        department.setId(1L);
        pk = new CompanyBranchDepartmentPK (companyBranch, department);

        Money companyBranchBudget = new Money(100000.0);
        companyBranch.setBudget(companyBranchBudget);

    }

    @BeforeEach
    void beforeEach() {
        dto = new DepartmentInfoDTO();
        dto.setDepartmentId(department.getId());
        dto.setCompanyBranchId(companyBranch.getId());
        bindingResult = new BeanPropertyBindingResult(dto, "dto");
        bindingResult = Mockito.spy(bindingResult);

        departmentInfo = new DepartmentInfo();
        departmentInfo.setPk(pk);
        departmentInfo.setDepartmentBudget(new Money(100000.0));
    }

    @Test
    void assert_that_no_errors_when_inserting_new_entry() {
        Mockito.when(departmentInfoService.existsById(pk)).thenReturn(false);
        Mockito.when(companyBranchService.getCompanyBranchById(dto.getCompanyBranchId())).thenReturn(companyBranch);
        Mockito.when(departmentService.getById(dto.getDepartmentId())).thenReturn(department);

        dto.setDepartmentBudget(new Money(1000.0));
        departmentInfoValidator.validate(dto, bindingResult);

        Assertions.assertFalse(bindingResult.hasErrors());
    }

    @Test
    void assert_that_can_not_insert_new_entry_when_budget_is_higher_then_company_branch_budget() {
        Mockito.when(departmentInfoService.existsById(pk)).thenReturn(false);
        Mockito.when(companyBranchService.getCompanyBranchById(dto.getCompanyBranchId())).thenReturn(companyBranch);
        Mockito.when(departmentService.getById(dto.getDepartmentId())).thenReturn(department);

        dto.setDepartmentBudget(new Money(1000000000.0));
        departmentInfoValidator.validate(dto, bindingResult);

        Assertions.assertTrue(bindingResult.hasErrors());
    }

    @Test
    void assert_that_updating_entry_is_not_validated_by_new_entry_validator() {
        Mockito.when(departmentInfoService.existsById(pk)).thenReturn(true);
        Mockito.when(companyBranchService.getCompanyBranchById(dto.getCompanyBranchId())).thenReturn(companyBranch);
        Mockito.when(departmentService.getById(dto.getDepartmentId())).thenReturn(department);
        departmentInfoValidator.getSubValidators().get(0).validate(dto, bindingResult);

        // Dto object comes with null value in departmentBudget field. If no exception is thrown in validate method
        // and no values are rejected, that means method returned after finding that dto is updating entry.
        Mockito.verify(bindingResult, Mockito.never()).rejectValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void asset_that_no_errors_when_updating_entry() {
        Mockito.when(departmentInfoService.existsById(pk)).thenReturn(true);
        Mockito.when(companyBranchService.getCompanyBranchById(dto.getCompanyBranchId())).thenReturn(companyBranch);
        Mockito.when(departmentService.getById(dto.getDepartmentId())).thenReturn(department);
        Mockito.when(departmentInfoService.getById(pk)).thenReturn(departmentInfo);

        dto.setDepartmentBudget(new Money(1000.0));
        departmentInfoValidator.validate(dto, bindingResult);

        Assertions.assertFalse(bindingResult.hasErrors());
    }

    @Test
    void assert_that_can_not_insert_updating_entry_when_budget_is_higher_then_company_branch_budget() {
        Mockito.when(departmentInfoService.existsById(pk)).thenReturn(true);
        Mockito.when(companyBranchService.getCompanyBranchById(dto.getCompanyBranchId())).thenReturn(companyBranch);
        Mockito.when(departmentService.getById(dto.getDepartmentId())).thenReturn(department);
        Mockito.when(departmentInfoService.getById(pk)).thenReturn(departmentInfo);

        dto.setDepartmentBudget(new Money(100000000.0));
        departmentInfoValidator.validate(dto, bindingResult);
        Assertions.assertTrue(bindingResult.hasErrors());
    }

    @Test
    void assert_that_new_entry_is_not_validated_by_updating_entry_validator() {
        Mockito.when(departmentInfoService.existsById(pk)).thenReturn(false);
        Mockito.when(companyBranchService.getCompanyBranchById(dto.getCompanyBranchId())).thenReturn(companyBranch);
        Mockito.when(departmentService.getById(dto.getDepartmentId())).thenReturn(department);
        departmentInfoValidator.getSubValidators().get(1).validate(dto, bindingResult);

        // Dto object comes with null value in departmentBudget field. If no exception is thrown in validate method
        // and no values are rejected, that means method returned after finding that dto is new entry.
        Mockito.verify(bindingResult, Mockito.never()).rejectValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

}
