package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.repository.CompanyBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

/**
 * Validator for CompanyBranchDTO
 */
@Component
public class CompanyBranchValidator extends BasicEntryValidation<CompanyBranchDTO> implements Validator {

    private final CompanyBranchRepository companyBranchRepository;

    @Autowired
    public CompanyBranchValidator(CompanyBranchRepository companyBranchRepository) {
        this.companyBranchRepository = companyBranchRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyBranchDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyBranchDTO companyBranchDTO = (CompanyBranchDTO) target;

        if (companyBranchDTO.getId() == null) {
           validateNewEntry(companyBranchDTO, errors);
        }else {
            validateUpdatingEntry(companyBranchDTO, errors);
        }
    }

    @Override
    protected void validateNewEntry(CompanyBranchDTO companyBranchDTO, Errors errors) {
        if (companyBranchRepository.existsByCompanyBranchAddress(companyBranchDTO.getCompanyBranchAddress()))
            errors.rejectValue("companyBranchAddress", "", "Филиал с таким адресом уже существует!");

        if (companyBranchRepository.existsByPhoneNumber(companyBranchDTO.getPhoneNumber()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }

    @Override
    protected void validateUpdatingEntry(CompanyBranchDTO companyBranchDTO, Errors errors) {
        Optional<CompanyBranch> companyBranchByAddress = companyBranchRepository
                .findCompanyBranchByCompanyBranchAddress(companyBranchDTO.getCompanyBranchAddress());
        Optional<CompanyBranch> companyBranchByPhone = companyBranchRepository
                .findCompanyBranchByPhoneNumber(companyBranchDTO.getPhoneNumber());

        if (companyBranchByAddress.isPresent() && !Objects.equals(companyBranchByAddress.get().getId(),
                companyBranchDTO.getId()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");

        if (companyBranchByPhone.isPresent() && !Objects.equals(companyBranchByPhone.get().getId(),
                companyBranchDTO.getId()))
            errors.rejectValue("phoneNumber", "", "Филиал с таким номером телефона уже существует!");
    }
}
