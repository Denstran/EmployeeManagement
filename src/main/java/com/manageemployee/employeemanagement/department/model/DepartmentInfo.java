package com.manageemployee.employeemanagement.department.model;

import com.manageemployee.employeemanagement.department.model.events.DepartmentInfoRegistered;
import com.manageemployee.employeemanagement.department.model.events.DepartmentInfoRemoved;
import com.manageemployee.employeemanagement.department.model.events.DepartmentInfoUpdated;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.converter.MoneyConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    public void registerDepartmentInfo() {
        registerEvent(new DepartmentInfoRegistered(this.pk.getCompanyBranch(), this.pk.getDepartment(),
                this.departmentBudget, this.departmentBudget));
    }

    public void updateDepartmentInfo(Money oldBudget) {
        registerEvent(new DepartmentInfoUpdated(this.pk.getCompanyBranch(), this.pk.getDepartment(),
                oldBudget, this.departmentBudget));
    }

    public void removeDepartmentInfo() {
        registerEvent(new DepartmentInfoRemoved(this.pk.getCompanyBranch(), this.pk.getDepartment(),
                this.departmentBudget, this.departmentBudget));
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
