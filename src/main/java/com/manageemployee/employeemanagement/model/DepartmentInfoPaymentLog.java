package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.model.enumTypes.EPaymentType;
import com.manageemployee.employeemanagement.model.enumTypes.TransferAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DEPARTMENT_INFO_PAYMENT_LOG")
@Getter
@Setter
public class DepartmentInfoPaymentLog extends BasePaymentEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_TYPE_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_DEPARTMENT_INFO_PAYMENT_LOG_PAYMENT_TYPE"))
    private PaymentType paymentType;

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
        departmentInfoPaymentLog.setPaymentType(new PaymentType(3L, EPaymentType.BUDGET_CHANGES));
        departmentInfoPaymentLog
                .setPaymentAmount(amount);
        departmentInfoPaymentLog.setTransferAction(isPositive ? TransferAction.INCREASE : TransferAction.DECREASE);
        return departmentInfoPaymentLog;
    }
}
