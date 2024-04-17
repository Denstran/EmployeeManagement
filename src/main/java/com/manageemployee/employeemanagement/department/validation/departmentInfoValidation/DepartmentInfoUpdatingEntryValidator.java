package com.manageemployee.employeemanagement.department.validation.departmentInfoValidation;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchService;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.validators.ValidatorQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@ValidatorQualifier(validatorKey = "departmentInfoSubValidator")
public class DepartmentInfoUpdatingEntryValidator implements Validator {
    private final DepartmentInfoService departmentInfoService;
    private final CompanyBranchService companyBranchService;
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentInfoUpdatingEntryValidator(DepartmentInfoService departmentInfoService,
                                                CompanyBranchService companyBranchService,
                                                DepartmentService departmentService) {
        this.departmentInfoService = departmentInfoService;
        this.companyBranchService = companyBranchService;
        this.departmentService = departmentService;
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

        if (!departmentInfoService.existsById(id)) return;

        DepartmentInfo departmentInfo = departmentInfoService.getById(id);

        if (dto.getDepartmentBudget().equals(departmentInfo.getDepartmentBudget())) return;

        Money budgetIncrease = Money.subtract(dto.getDepartmentBudget(), departmentInfo.getDepartmentBudget());

        if (Money.compareTo(companyBranch.getBudget(), budgetIncrease) < 0)
            errors.rejectValue("departmentBudget", "", "Повышение бюджета превышает бюджет филиала!");

    }
}
