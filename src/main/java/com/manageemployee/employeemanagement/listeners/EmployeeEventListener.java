package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.model.events.employeeEvents.*;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmployeeEventListener {
    private final DepartmentInfoService departmentInfoService;
    private final MoneyService moneyService;

    @Autowired
    public EmployeeEventListener(DepartmentInfoService departmentInfoService, MoneyService moneyService) {
        this.departmentInfoService = departmentInfoService;
        this.moneyService = moneyService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createEmployeeEventHandler(EmployeeHired employeeHired) {
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeHired);

        Money newDepartmentBudget =
                moneyService.subtract(departmentInfo.getDepartmentBudget(), employeeHired.getSalary());

        departmentInfo.setDepartmentBudget(newDepartmentBudget);
        departmentInfoService.update(departmentInfo);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateEmployeeEventHandler(EmployeeUpdated employeeUpdated) {
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeUpdated);
        Money oldSalary = employeeUpdated.getOldSalary();
        Money newSalary = employeeUpdated.getSalary();

        Money amountToSubtract = moneyService.subtract(newSalary, oldSalary);
        departmentInfo.setDepartmentBudget(moneyService.subtract(departmentInfo.getDepartmentBudget(), amountToSubtract));

        departmentInfoService.update(departmentInfo);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void deleteEmployeeEventHandler(EmployeeDeleted employeeDeleted) {
        if (employeeDeleted.getEmployeeStatus().getEmployeeStatus().equals(EEmployeeStatus.FIRED))
            return;
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeDeleted);

        departmentInfo.setDepartmentBudget(moneyService.sum(departmentInfo.getDepartmentBudget(),
                employeeDeleted.getSalary()));
        departmentInfoService.update(departmentInfo);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void fireEmployeeEventHandler(EmployeeFired employeeFired) {
        DepartmentInfo departmentInfo = getDepartmentInfo(employeeFired);

        departmentInfo.setDepartmentBudget(moneyService.sum(departmentInfo.getDepartmentBudget(),
                employeeFired.getSalary()));
        departmentInfoService.update(departmentInfo);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeBaseEvent event) {
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(event.getCompanyBranch(), event.getDepartment())
        );
    }
}