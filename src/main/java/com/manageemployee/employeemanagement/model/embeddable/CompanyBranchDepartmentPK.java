package com.manageemployee.employeemanagement.model.embeddable;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Department;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class CompanyBranchDepartmentPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "COMPANY_BRANCH_ID")
    private CompanyBranch companyBranch;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;
}
