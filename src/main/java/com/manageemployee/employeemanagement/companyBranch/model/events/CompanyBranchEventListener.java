package com.manageemployee.employeemanagement.companyBranch.model.events;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchPaymentLogService;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.MoneyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CompanyBranchEventListener {
    private final CompanyBranchPaymentLogService paymentLogService;

    @Autowired
    public CompanyBranchEventListener(CompanyBranchPaymentLogService paymentLogService) {
        this.paymentLogService = paymentLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void companyBranchCreatedEventHandler(CompanyBranchCreated companyBranchCreated) {
        CompanyBranchPaymentLog paymentLog = CompanyBranchPaymentLog
                .createPaymentLog(companyBranchCreated.getCompanyBranch(),
                        companyBranchCreated.getCompanyBranch().getBudget(), true);

        paymentLogService.saveCompanyBranchPaymentLog(paymentLog);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void companyBranchUpdatedEventHandler(CompanyBranchUpdated companyBranchUpdated) {
        CompanyBranch updatedCompanyBranch = companyBranchUpdated.getCompanyBranch();
        Money oldBudget = companyBranchUpdated.getOldBudget();
        if (updatedCompanyBranch.getBudget().equals(oldBudget)) return;

        Money amountToReduce = MoneyUtil.subtract(updatedCompanyBranch.getBudget(), oldBudget);

        CompanyBranchPaymentLog paymentLog =
                CompanyBranchPaymentLog.createPaymentLog(updatedCompanyBranch,
                        MoneyUtil.abs(amountToReduce), MoneyUtil.isPositive(amountToReduce));

        paymentLogService.saveCompanyBranchPaymentLog(paymentLog);
    }
}
