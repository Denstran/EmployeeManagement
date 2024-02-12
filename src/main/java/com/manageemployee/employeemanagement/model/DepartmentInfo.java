package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.model.events.DepartmentInfoRegistered;
import com.manageemployee.employeemanagement.model.events.DepartmentInfoRemoved;
import com.manageemployee.employeemanagement.model.events.DepartmentInfoUpdated;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Objects;

@Entity
@Table(name = "DEPARTMENT_INFO")
@Getter
@Setter
public class DepartmentInfo extends AbstractAggregateRoot<DepartmentInfo> {

    @EmbeddedId
    private CompanyBranchDepartmentPK pk;

    @Column(name = "DEPARTMENT_BUDGET")
    @Convert(converter = MoneyConverter.class)
    private Money departmentBudget;

    @Formula("SELECT COUNT(*) FROM EMPLOYEE AS e WHERE e.COMPANY_BRANCH_ID = COMPANY_BRANCH_ID AND " +
            "e.POSITION_ID IN (SELECT p.ID FROM POSITION AS p WHERE p.DEP_ID = DEPARTMENT_ID)")
    private int amountOfEmployee;

    public void registerDepartmentInfo() {
        registerEvent(new DepartmentInfoRegistered(this.departmentBudget, this.pk.getCompanyBranch()));
    }

    public void updateDepartmentInfo(Money oldBudget) {
        DepartmentInfoUpdated departmentInfoUpdated =
                new DepartmentInfoUpdated(this.getPk().getCompanyBranch(), oldBudget, this.departmentBudget);
        registerEvent(departmentInfoUpdated);
    }

    public void removeDepartmentInfo() {
        registerEvent(new DepartmentInfoRemoved(this.pk.getCompanyBranch(), this.departmentBudget));
    }

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
