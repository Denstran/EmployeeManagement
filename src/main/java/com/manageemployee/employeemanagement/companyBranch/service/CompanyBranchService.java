package com.manageemployee.employeemanagement.companyBranch.service;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.repository.CompanyBranchRepository;
import com.manageemployee.employeemanagement.util.Address;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.MoneyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyBranchService {
    private final CompanyBranchRepository companyBranchRepository;

    public CompanyBranchService(CompanyBranchRepository companyBranchRepository) {
        this.companyBranchRepository = companyBranchRepository;
    }

    @Transactional
    public CompanyBranch createCompanyBranch(CompanyBranch companyBranch) {
        if (companyBranch == null)
            throw new IllegalArgumentException("Некорректный объект для создания филиала!");

        companyBranch.createCompanyBranch();
        return  companyBranchRepository.saveAndFlush(companyBranch);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompanyBranch updateCompanyBranch(CompanyBranch companyBranch) {
        if (companyBranch == null)
            throw new IllegalArgumentException("Выбран некорректный филиал для обновления!");
        CompanyBranch companyBranchFromDB = getCompanyBranchById(companyBranch.getId());
        companyBranch.updateCompanyBranch(companyBranchFromDB);

        return companyBranchRepository.saveAndFlush(companyBranch);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void allocateBudget(CompanyBranch companyBranch, Money budgetChanges) {
        Money reducedBudget = MoneyUtil.subtract(companyBranch.getBudget(), budgetChanges);
        companyBranch.setBudget(reducedBudget);
        companyBranchRepository.saveAndFlush(companyBranch);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseBudget(CompanyBranch companyBranch, Money budgetChanges) {
        Money increasedBudget = MoneyUtil.sum(companyBranch.getBudget(), budgetChanges);
        companyBranch.setBudget(increasedBudget);
        companyBranchRepository.saveAndFlush(companyBranch);
    }

    public CompanyBranch getCompanyBranchById(Long id) {
        return companyBranchRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Выбран несуществующий филиал!"));
    }

    public CompanyBranch getReference(Long id) {
        if (id == null || id < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");

        return companyBranchRepository.getReferenceById(id);
    }

    public List<CompanyBranch> getAllCompanyBranches(){
        return companyBranchRepository.findAll();
    }

    @Transactional
    public void deleteCompanyBranchById(Long id) {
        if (id == null || id < 1)
            throw new IllegalArgumentException("Выбран несуществующий филиал!");
        CompanyBranch companyBranch = getCompanyBranchById(id);
        companyBranch.deleteCompanyBranch();
        companyBranchRepository.delete(companyBranch);
    }

    public Optional<CompanyBranch> getByPhoneNumber(String phoneNumber) {
        return companyBranchRepository.findCompanyBranchByPhoneNumber(phoneNumber);
    }

    public Optional<CompanyBranch> getByAddress(Address address) {
        return companyBranchRepository.findCompanyBranchByCompanyBranchAddress(address);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) return false;

        return companyBranchRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean existsByAddress(Address address) {
        if (address == null) return false;

        return companyBranchRepository.existsByCompanyBranchAddress(address);
    }
}
