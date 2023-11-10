package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.repository.CompanyBranchRepository;
import com.manageemployee.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Component
public class EmployeeValidator extends BasicEntryValidation<EmployeeDTO> implements Validator {
    private final EmployeeRepository employeeRepository;
    private final CompanyBranchRepository companyBranchRepository;

    @Autowired
    public EmployeeValidator(EmployeeRepository employeeRepository, CompanyBranchRepository companyBranchRepository) {
        this.employeeRepository = employeeRepository;
        this.companyBranchRepository = companyBranchRepository;
    }

    @Override
    protected void validateNewEntry(EmployeeDTO employeeDTO, Errors errors) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail()))
            errors.rejectValue("email", "", "Работник с таким email уже существует!");

        if (employeeRepository.existsByPhoneNumber(employeeDTO.getPhoneNumber()))
            errors.rejectValue("phoneNumber", "", "Работник с таким номером телефона уже существует!");

        if (employeeDTO.getSalary().getAmount() != null)
            validateMoney(employeeDTO, errors);

    }

    @Override
    protected void validateUpdatingEntry(EmployeeDTO employeeDTO, Errors errors) {
        Optional<Employee> employeeByEmail = employeeRepository.findEmployeeByEmail(employeeDTO.getEmail());

        Optional<Employee> employeeByPhoneNumber = employeeRepository.findEmployeeByPhoneNumber(
                employeeDTO.getPhoneNumber());

        if (employeeByEmail.isPresent() && !Objects.equals(employeeByEmail.get().getId(), employeeDTO.getId()))
            errors.rejectValue("email", "", "Работник с таким email уже существует!");

        if (employeeByPhoneNumber.isPresent() && !Objects.equals(employeeByPhoneNumber.get().getId(),
                employeeDTO.getId()))
            errors.rejectValue("phoneNumber", "", "Работник с таким номером телефона уже существует!");

        if (employeeDTO.getSalary().getAmount() != null)
            validateMoney(employeeDTO, errors);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmployeeDTO employeeDTO = (EmployeeDTO) target;

        if (employeeDTO.getId() == null) {
            validateNewEntry(employeeDTO, errors);
        } else validateUpdatingEntry(employeeDTO, errors);
    }

    private void validateMoney(EmployeeDTO employeeDTO, Errors errors) {
        Optional<CompanyBranch> companyBranch = companyBranchRepository
                .findCompanyBranchByDepartmentId(employeeDTO.getDepartmentId());

        if (companyBranch.isPresent()) {
            if (employeeDTO.getId() != null) {
                validateMoneyForUpdatedEntry(employeeDTO, companyBranch.get(), errors);
            } else {
                validateMoneyForNewEntry(companyBranch.get(), employeeDTO, errors);
            }
        }
    }

    private void validateMoneyForNewEntry(CompanyBranch companyBranch, EmployeeDTO employeeDTO, Errors errors) {
        if (companyBranch.getBudget().getAmount().compareTo(employeeDTO.getSalary().getAmount()) < 0) {
            errors.rejectValue("salary.amount", "", "Зарплата превышает бюджет!");
        }

        if (!companyBranch.getBudget().getCurrency().equals(employeeDTO.getSalary().getCurrency())) {
            errors.rejectValue("salary.currency", "", "Валюта зарплаты не соответсвует валюте бюджета филиала!");
        }
    }

    private void validateMoneyForUpdatedEntry(EmployeeDTO employeeDTO, CompanyBranch companyBranch, Errors errors) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeDTO.getId());

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            if (employeeDTO.getSalary().getAmount().compareTo(employee.getSalary().getAmount()) > 0) {
                BigDecimal salaryIncrease = employeeDTO.getSalary().getAmount()
                        .subtract(employee.getSalary().getAmount());

                if (companyBranch.getBudget().getAmount().compareTo(salaryIncrease) < 0)
                    errors.rejectValue("salary.amount", "", "Надбавка превышает бюджет!");
            }
        }
    }
}
