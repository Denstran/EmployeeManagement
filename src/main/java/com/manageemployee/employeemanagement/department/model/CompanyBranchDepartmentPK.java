package com.manageemployee.employeemanagement.department.model;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyBranchDepartmentPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "COMPANY_BRANCH_ID", nullable = false)
    @NotNull(message = "Филиал не должен отсутствовать!")
    private CompanyBranch companyBranch;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false)
    @NotNull(message = "Отдел не должен отсутствовать!")
    private Department department;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyBranchDepartmentPK pk)) return false;
        return Objects.equals(companyBranch, pk.companyBranch) && Objects.equals(department, pk.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyBranch, department);
    }
}
