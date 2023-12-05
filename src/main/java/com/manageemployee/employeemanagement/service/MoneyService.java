package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoneyService {
    private final CompanyBranchService companyBranchService;
    private final EmployeeService employeeService;

    public MoneyService(CompanyBranchService companyBranchService,
                        EmployeeService employeeService) {
        this.companyBranchService = companyBranchService;
        this.employeeService = employeeService;
    }

    @Transactional
    public void handleEmployeeSalaryChanges(EmployeeDTO employeeDTO, Long companyBranchId) {
        if (employeeDTO == null)
            throw new IllegalArgumentException("Передан невалидный сотрудник!");

        CompanyBranch companyBranch = companyBranchService.getCompanyBranchById(companyBranchId);
        if (employeeDTO.getId() == null) {
            companyBranch.setBudget(subtractMoney(companyBranch.getBudget(), employeeDTO.getSalary()));
        }else {
            Employee employeeOld = employeeService.getEmployeeById(employeeDTO.getId());
            if (employeeDTO.getSalary().compareTo(employeeOld.getSalary()) > 0)
                handleIncreasedSalary(employeeDTO, employeeOld, companyBranch);
            else handleDecreasedSalary(employeeDTO, employeeOld, companyBranch);
        }

        companyBranchService.updateCompanyBranch(companyBranch);
    }

    private void handleIncreasedSalary(EmployeeDTO employeeDTO, Employee employeeOld, CompanyBranch companyBranch) {
        Money salaryIncrease = subtractMoney(employeeDTO.getSalary(), employeeOld.getSalary());
        companyBranch.setBudget(subtractMoney(companyBranch.getBudget(), salaryIncrease));
    }

    private void handleDecreasedSalary(EmployeeDTO employeeDTO, Employee employeeOld, CompanyBranch companyBranch) {
        Money salaryDecrease = subtractMoney(employeeOld.getSalary(), employeeDTO.getSalary());
        companyBranch.setBudget(addMoney(companyBranch.getBudget(), salaryDecrease));
    }

    public Money addMoney(Money moneyToAddTo, Money moneyForAdding) {
        checkCurrency(moneyToAddTo, moneyForAdding);

        return new Money(moneyToAddTo.getAmount().add(moneyForAdding.getAmount()), moneyToAddTo.getCurrency());
    }

    public Money subtractMoney(Money moneyToSubtractFrom, Money forSubtract) {
        checkCurrency(moneyToSubtractFrom, forSubtract);

        return new Money(moneyToSubtractFrom.getAmount().subtract(forSubtract.getAmount()),
                moneyToSubtractFrom.getCurrency());
    }

    private void checkCurrency(Money first, Money second) {
        if (!first.getCurrency().equals(second.getCurrency()))
            throw new UnsupportedOperationException("Арифметические операции для работы с разными валютами не поддерживаются!");
    }
}
