package com.manageemployee.employeemanagement.companyBranch.model;

import com.manageemployee.employeemanagement.util.BasePaymentEntity;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.enumType.PaymentType;
import com.manageemployee.employeemanagement.util.enumType.TransferAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "COMPANY_BRANCH_PAYMENT_LOG")
@Getter
@Setter
public class CompanyBranchPaymentLog extends BasePaymentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_BRANCH_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_COMPANY_BRANCH_PAYMENT_LOG"))
    private CompanyBranch companyBranch;

    public static CompanyBranchPaymentLog createPaymentLog(CompanyBranch companyBranch, Money amount, boolean isPositive) {
        CompanyBranchPaymentLog companyBranchPaymentLog = new CompanyBranchPaymentLog();
        companyBranchPaymentLog.setCompanyBranch(companyBranch);
        companyBranchPaymentLog.setPaymentType(PaymentType.BUDGET_CHANGES);
        companyBranchPaymentLog
                .setPaymentAmount(amount);
        companyBranchPaymentLog.setTransferAction(isPositive ? TransferAction.INCREASE : TransferAction.DECREASE);
        return  companyBranchPaymentLog;
    }
}
