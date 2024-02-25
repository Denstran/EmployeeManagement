package com.manageemployee.employeemanagement.listeners;

import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoRegistered;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoRemoved;
import com.manageemployee.employeemanagement.model.events.departmentEvents.DepartmentInfoUpdated;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DepartmentInfoEventListener {
    private final CompanyBranchService companyBranchService;
    private final MoneyService moneyService;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentInfoEventListener(CompanyBranchService companyBranchService, MoneyService moneyService, EmployeeService employeeService) {
        this.companyBranchService = companyBranchService;
        this.moneyService = moneyService;
        this.employeeService = employeeService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void reduceCompanyBranchBudget(DepartmentInfoRegistered departmentInfoRegistered) {
        CompanyBranch companyBranch = departmentInfoRegistered.getCompanyBranch();
        Money departmentBudget = departmentInfoRegistered.getDepartmentBudget();

        companyBranch.setBudget(moneyService.subtract(companyBranch.getBudget(), departmentBudget));
        companyBranchService.updateCompanyBranch(companyBranch);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateCompanyBranchBudget(DepartmentInfoUpdated departmentInfoUpdated) {
        CompanyBranch companyBranch = departmentInfoUpdated.getCompanyBranch();

        Money oldDepBudget = departmentInfoUpdated.getOldDepartmentBudget();
        Money newDepBudget = departmentInfoUpdated.getNewDepartmentBudget();
        Money amountToReduce = moneyService.subtract(newDepBudget, oldDepBudget);

        companyBranch.setBudget(moneyService.subtract(companyBranch.getBudget(), amountToReduce));
        companyBranchService.updateCompanyBranch(companyBranch);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void addCompanyBranchBudget(DepartmentInfoRemoved departmentInfoRemoved) {
        CompanyBranch companyBranch = departmentInfoRemoved.getCompanyBranch();
        Money employeeSalaries =
                employeeService.countEmployeeSalariesByCompanyBranchAndDepartment(companyBranch,
                                departmentInfoRemoved.getDepartment());

        companyBranch.setBudget(
                moneyService.sum(companyBranch.getBudget(), departmentInfoRemoved.getDepartmentBudget()));
        companyBranch.setBudget(moneyService.sum(companyBranch.getBudget(), employeeSalaries));

        employeeService.deleteAllByCompanyBranchAndDepartment(companyBranch, departmentInfoRemoved.getDepartment());
        companyBranchService.updateCompanyBranch(companyBranch);
    }
}
