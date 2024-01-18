package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.Objects;

@Entity
@Table(name = "DEPARTMENT_INFO")
@Getter
@Setter
public class DepartmentInfo {

    @EmbeddedId
    private CompanyBranchDepartmentPK pk;

    @Column(name = "DEPARTMENT_BUDGET")
    @Convert(converter = MoneyConverter.class)
    private Money departmentBudget;

    @Formula("SELECT COUNT(*) FROM EMPLOYEE AS e WHERE e.COMPANY_BRANCH_ID = COMPANY_BRANCH_ID AND " +
            "e.POSITION_ID IN (SELECT p.ID FROM POSITION AS p WHERE p.DEP_ID = DEPARTMENT_ID)")
    private int amountOfEmployee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentInfo that)) return false;
        return Objects.equals(pk, that.pk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk);
    }
}
