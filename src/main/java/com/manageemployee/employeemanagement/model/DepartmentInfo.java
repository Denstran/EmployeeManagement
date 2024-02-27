package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.model.enumTypes.EPaymentType;
import com.manageemployee.employeemanagement.model.enumTypes.TransferAction;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoRegistered;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoRemoved;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoUpdated;
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
        DepartmentInfoPaymentLog departmentInfoPaymentLog = new DepartmentInfoPaymentLog();
        departmentInfoPaymentLog.setDepartment(this.pk.getDepartment());
        departmentInfoPaymentLog.setCompanyBranch(this.pk.getCompanyBranch());
        departmentInfoPaymentLog.setPaymentType(new PaymentType(3L, EPaymentType.BUDGET_CHANGES));
        departmentInfoPaymentLog.setPaymentAmount(this.departmentBudget);
        departmentInfoPaymentLog.setTransferAction(TransferAction.INCREASE);

        registerEvent(new DepartmentInfoRegistered(departmentInfoPaymentLog));
    }

    public void updateDepartmentInfo(Money oldBudget) {
        DepartmentInfoUpdated departmentInfoUpdated =
                new DepartmentInfoUpdated(this.getPk().getCompanyBranch(), this.pk.getDepartment(),
                        oldBudget, this.departmentBudget);
        registerEvent(departmentInfoUpdated);
    }

    public void removeDepartmentInfo() {
        registerEvent(new DepartmentInfoRemoved(this.pk.getCompanyBranch(), this.pk.getDepartment(),
                this.departmentBudget));
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
