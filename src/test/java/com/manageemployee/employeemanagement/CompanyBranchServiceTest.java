package com.manageemployee.employeemanagement;

import com.manageemployee.employeemanagement.exception.ResourceAlreadyExistsException;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.repository.CompanyBranchRepository;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        companyBranch.setPhoneNumber("111");
        Mockito.when(repo.findCompanyBranchByPhoneNumber("111")).thenReturn(companyBranch);

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
}
