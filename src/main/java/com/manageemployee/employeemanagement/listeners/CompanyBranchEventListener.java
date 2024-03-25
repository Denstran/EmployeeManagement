package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.events.companyBranchEvents.CompanyBranchCreated;
import com.manageemployee.employeemanagement.model.events.companyBranchEvents.CompanyBranchUpdated;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CompanyBranchEventListener {
    private final PaymentLogService paymentLogService;

    @Autowired
    public CompanyBranchEventListener(PaymentLogService paymentLogService) {
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

        Money amountToReduce = MoneyService.subtract(updatedCompanyBranch.getBudget(), oldBudget);

        CompanyBranchPaymentLog paymentLog =
                CompanyBranchPaymentLog.createPaymentLog(updatedCompanyBranch,
                        MoneyService.abs(amountToReduce), MoneyService.isPositive(amountToReduce));

        paymentLogService.saveCompanyBranchPaymentLog(paymentLog);
    }
}
