package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.CompanyBranchPaymentLog;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.events.companyBranchEvents.CompanyBranchCreated;
import com.manageemployee.employeemanagement.model.events.companyBranchEvents.CompanyBranchDeleted;
import com.manageemployee.employeemanagement.model.events.companyBranchEvents.CompanyBranchUpdated;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CompanyBranchEventListener {
    private final EmployeeService employeeService;
    private final PaymentLogService paymentLogService;
    private final DepartmentInfoService departmentInfoService;
    private final MoneyService moneyService;

    @Autowired
    public CompanyBranchEventListener(EmployeeService employeeService, PaymentLogService paymentLogService,
                                      DepartmentInfoService departmentInfoService, MoneyService moneyService) {
        this.employeeService = employeeService;
        this.paymentLogService = paymentLogService;
        this.departmentInfoService = departmentInfoService;
        this.moneyService = moneyService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void companyBranchDeletedEvenHandler(CompanyBranchDeleted companyBranchDeleted) {
        paymentLogService.deleteEmployeePaymentLogsByCompanyBranch(companyBranchDeleted.getCompanyBranch());
        paymentLogService.deleteDepartmentPaymentLogsByCompanyBranch(companyBranchDeleted.getCompanyBranch());
        paymentLogService.deleteCompanyBranchPaymentLogsByCompanyBranch(companyBranchDeleted.getCompanyBranch());
        employeeService.deleteAllByCompanyBranch(companyBranchDeleted.getCompanyBranch());
        departmentInfoService.deleteAllByCompanyBranch(companyBranchDeleted.getCompanyBranch());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
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

        Money amountToReduce = moneyService.subtract(updatedCompanyBranch.getBudget(), oldBudget);

        CompanyBranchPaymentLog paymentLog =
                CompanyBranchPaymentLog.createPaymentLog(updatedCompanyBranch,
                        moneyService.abs(amountToReduce), moneyService.isPositive(amountToReduce));

        paymentLogService.saveCompanyBranchPaymentLog(paymentLog);
    }
}
