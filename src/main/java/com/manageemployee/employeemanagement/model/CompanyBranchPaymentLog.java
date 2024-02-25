package com.manageemployee.employeemanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "COMPANY_BRANCH_PAYMENT_LOG")
@Getter
@Setter
public class CompanyBranchPaymentLog extends BasePaymentEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_TYPE_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PAYMENT_COMPANY_BRANCH_PAYMENT_TYPE"))
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_BRANCH_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_COMPANY_BRANCH_PAYMENT_LOG"))
    private CompanyBranch companyBranch;
}
