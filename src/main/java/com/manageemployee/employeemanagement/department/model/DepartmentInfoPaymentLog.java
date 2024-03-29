package com.manageemployee.employeemanagement.department.model;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.util.BasePaymentEntity;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.enumType.PaymentType;
import com.manageemployee.employeemanagement.util.enumType.TransferAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DEPARTMENT_INFO_PAYMENT_LOG")
@Getter
@Setter
public class DepartmentInfoPaymentLog extends BasePaymentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_BRANCH_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_COMPANY_BRANCH_DEPARTMENT_INFO_PAYMENT_LOG"))
    private CompanyBranch companyBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_DEPARTMENT_DEPARTMENT_INFO_PAYMENT_LOG"))
    private Department department;

    public static DepartmentInfoPaymentLog createPaymentLog(CompanyBranch companyBranch, Department department,
                                                            Money amount, boolean isPositive) {
        DepartmentInfoPaymentLog departmentInfoPaymentLog = new DepartmentInfoPaymentLog();
        departmentInfoPaymentLog.setCompanyBranch(companyBranch);
        departmentInfoPaymentLog.setDepartment(department);
        departmentInfoPaymentLog.setPaymentType(PaymentType.BUDGET_CHANGES);
        departmentInfoPaymentLog
                .setPaymentAmount(amount);
        departmentInfoPaymentLog.setTransferAction(isPositive ? TransferAction.INCREASE : TransferAction.DECREASE);
        return departmentInfoPaymentLog;
    }

    public static DepartmentInfoPaymentLog createPaymentLog(CompanyBranchDepartmentPK pk, Money amount,
                                                            boolean isPositive) {
        return createPaymentLog(pk.getCompanyBranch(), pk.getDepartment(), amount, isPositive);
    }
}
