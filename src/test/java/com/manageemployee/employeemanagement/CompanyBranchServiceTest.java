package com.manageemployee.employeemanagement;

import com.manageemployee.employeemanagement.exception.ResourceAlreadyExistsException;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.repository.CompanyBranchRepository;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class CompanyBranchServiceTest {
    private CompanyBranchService companyBranchService;
    private static final CompanyBranchRepository repo = Mockito.mock(CompanyBranchRepository.class);

    @BeforeEach
    void setUp() {
        companyBranchService = new CompanyBranchService(repo);
    }

    @Test
    void shouldThrowResourceAlreadyExistsWhenTryingToInsertEntryWithExistingPhoneNumber(){
        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setPhoneNumber("333");
        Mockito.when(repo.findCompanyBranchByPhoneNumber("333")).thenReturn(companyBranch);

        Assertions.assertThrows(ResourceAlreadyExistsException.class,
                () -> companyBranchService.createCompanyBranch(companyBranch));
    }

    @Test
    void shouldThrowResourceAlreadyExistsWhenTryingToInsertEntryWithExistingAddress() {
        CompanyBranch companyBranch = new CompanyBranch();
        Address address = new Address();
        address.setCity("Test");
        companyBranch.setCompanyBranchAddress(address);

        Mockito.when(repo.findCompanyBranchByCompanyBranchAddress(address)).thenReturn(companyBranch);

        Assertions.assertThrows(ResourceAlreadyExistsException.class,
                () -> companyBranchService.createCompanyBranch(companyBranch));
    }

    @Test
    void shouldSaveEntity(){
        CompanyBranch companyBranch = new CompanyBranch();
        Mockito.when(repo.saveAndFlush(companyBranch)).thenReturn(companyBranch);
        CompanyBranch result = companyBranchService.createCompanyBranch(companyBranch);

        Assertions.assertSame(result, companyBranch);
    }

    @Test
    void shouldDeleteEntity() {
        Long companyBranchId = 1L;
        BDDMockito.willDoNothing().given(repo).deleteById(companyBranchId);

        companyBranchService.deleteCompanyBranchById(companyBranchId);

        BDDMockito.verify(repo, Mockito.times(1)).deleteById(companyBranchId);
    }

    @Test
    void shouldUpdateEntity() {
        CompanyBranch companyBranch = new CompanyBranch();
        Mockito.when(repo.saveAndFlush(companyBranch)).thenReturn(companyBranch);
        companyBranch.setPhoneNumber("111");

        CompanyBranch updatedCompanyBranch = companyBranchService.updateCompanyBranch(companyBranch);

        Assertions.assertEquals(updatedCompanyBranch.getPhoneNumber(), "111");
    }

    @Test
    void shouldThrowResourceAlreadyExistsWhenTryingToUpdateEntryWithExistingPhoneNumber() {
        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setId(1L);
        companyBranch.setPhoneNumber("222");
        CompanyBranch existingCompanyBranch = new CompanyBranch();
        existingCompanyBranch.setId(2L);
        existingCompanyBranch.setPhoneNumber("222");

        Mockito.when(repo.findCompanyBranchByPhoneNumber(companyBranch.getPhoneNumber()))
                .thenReturn(existingCompanyBranch);

        Assertions.assertThrows(ResourceAlreadyExistsException.class, () ->
                companyBranchService.updateCompanyBranch(companyBranch));

    }

    @Test
    void shouldThrowResourceAlreadyExistsWhenTryingToUpdateEntryWithExistingAddress() {
        Address address = new Address();
        address.setCity("SPB");

        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setId(1L);
        companyBranch.setCompanyBranchAddress(address);
        CompanyBranch existingCompanyBranch = new CompanyBranch();
        existingCompanyBranch.setId(2L);
        existingCompanyBranch.setCompanyBranchAddress(address);

        Mockito.when(repo.findCompanyBranchByCompanyBranchAddress(companyBranch.getCompanyBranchAddress()))
                .thenReturn(existingCompanyBranch);

        Assertions.assertThrows(ResourceAlreadyExistsException.class, () ->
                companyBranchService.updateCompanyBranch(companyBranch));
    }
}
