package com.manageemployee.employeemanagement.util.validators.departmentInfoValidators;

import com.manageemployee.employeemanagement.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@ValidatorQualifier(validatorKey = "departmentInfoSubValidator")
public class DepartmentInfoNewEntryValidator implements Validator {

    private final CompanyBranchService companyBranchService;
    private final DepartmentService departmentService;
    private final DepartmentInfoService departmentInfoService;
    private final MoneyService moneyService;

    @Autowired
    public DepartmentInfoNewEntryValidator(CompanyBranchService companyBranchService,
                                           DepartmentService departmentService,
                                           DepartmentInfoService departmentInfoService,
                                           MoneyService moneyService) {
        this.companyBranchService = companyBranchService;
        this.departmentService = departmentService;
        this.departmentInfoService = departmentInfoService;
        this.moneyService = moneyService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentInfoDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        DepartmentInfoDTO dto = (DepartmentInfoDTO) target;
        CompanyBranch companyBranch = companyBranchService.getCompanyBranchById(dto.getCompanyBranchId());
        Department department = departmentService.getById(dto.getDepartmentId());
        CompanyBranchDepartmentPK id = new CompanyBranchDepartmentPK(companyBranch, department);
        if (departmentInfoService.existsById(id)) return;

        if (!dto.getDepartmentBudget().getCurrency().equals(companyBranch.getBudget().getCurrency()))
            errors.rejectValue("departmentBudget", "", "Валюта не совпадает с валютой филиала!");

        if (moneyService.compareAmounts(companyBranch.getBudget(), dto.getDepartmentBudget()) < 0)
            errors.rejectValue("departmentBudget", "", "Выделенный бюджет превышает бюджет филиала!");
    }
}
