package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.model.EmployeePaymentLog;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.model.events.employeeEvents.*;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmployeeEventListener {
    private final DepartmentInfoService departmentInfoService;
    private final MoneyService moneyService;
    private final PaymentLogService paymentLogService;

    @Autowired
    public EmployeeEventListener(DepartmentInfoService departmentInfoService, MoneyService moneyService,
                                 PaymentLogService paymentLogService) {
        this.departmentInfoService = departmentInfoService;
        this.moneyService = moneyService;
        this.paymentLogService = paymentLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createEmployeeEventHandler(EmployeeHired employeeHired) {
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeHired);
        Money newDepartmentBudget =
                moneyService.subtract(departmentInfo.getDepartmentBudget(), employeeHired.getSalary());
        EmployeePaymentLog employeePaymentLog = EmployeePaymentLog
                .createPaymentLog(employeeHired.getEmployee(), employeeHired.getSalary(), true);
        DepartmentInfoPaymentLog departmentInfoPaymentLog =
                DepartmentInfoPaymentLog.createPaymentLog(employeeHired.getCompanyBranch(),
                        employeeHired.getDepartment(), employeeHired.getSalary(), false);
        departmentInfo.setDepartmentBudget(newDepartmentBudget);
        departmentInfoService.update(departmentInfo);
        paymentLogService.saveDepartmentInfoPaymentLog(departmentInfoPaymentLog);
        paymentLogService.saveEmployeePaymentLog(employeePaymentLog);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateEmployeeEventHandler(EmployeeUpdated employeeUpdated) {
        Money oldSalary = employeeUpdated.getOldSalary();
        Money newSalary = employeeUpdated.getSalary();
        if (oldSalary.equals(newSalary)) return;
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeUpdated);

        Money amountToSubtract = moneyService.subtract(newSalary, oldSalary);
        EmployeePaymentLog employeePaymentLog =
                EmployeePaymentLog.createPaymentLog(employeeUpdated.getEmployee(), moneyService.abs(amountToSubtract),
                        moneyService.isPositive(amountToSubtract));

        DepartmentInfoPaymentLog departmentInfoPaymentLog =
                DepartmentInfoPaymentLog.createPaymentLog(employeeUpdated.getCompanyBranch(),
                        employeeUpdated.getDepartment(), moneyService.abs(amountToSubtract),
                        !moneyService.isPositive(amountToSubtract));

        departmentInfo.setDepartmentBudget(moneyService.subtract(departmentInfo.getDepartmentBudget(), amountToSubtract));
        departmentInfoService.update(departmentInfo);
        paymentLogService.saveDepartmentInfoPaymentLog(departmentInfoPaymentLog);
        paymentLogService.saveEmployeePaymentLog(employeePaymentLog);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void deleteEmployeeEventHandler(EmployeeDeleted employeeDeleted) {
        if (employeeDeleted.getEmployeeStatus().getEmployeeStatus().equals(EEmployeeStatus.FIRED))
            return;
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeDeleted);

        departmentInfo.setDepartmentBudget(moneyService.sum(departmentInfo.getDepartmentBudget(),
                employeeDeleted.getSalary()));

        DepartmentInfoPaymentLog paymentLog =
                DepartmentInfoPaymentLog.createPaymentLog(employeeDeleted.getCompanyBranch(),
                        employeeDeleted.getDepartment(), employeeDeleted.getSalary(), true);

        paymentLogService.deleteEmployeePaymentLogs(employeeDeleted.getEmployee().getId());
        departmentInfoService.update(departmentInfo);
        paymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void fireEmployeeEventHandler(EmployeeFired employeeFired) {
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeFired);

        DepartmentInfoPaymentLog paymentLog =
                DepartmentInfoPaymentLog.createPaymentLog(employeeFired.getCompanyBranch(),
                        employeeFired.getDepartment(), employeeFired.getSalary(), true);
        departmentInfo.setDepartmentBudget(moneyService.sum(departmentInfo.getDepartmentBudget(),
                employeeFired.getSalary()));
        departmentInfoService.update(departmentInfo);
        paymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeBaseEvent event) {
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(event.getCompanyBranch(), event.getDepartment())
        );
    }
}
