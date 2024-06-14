package com.manageemployee.employeemanagement.employee.event.employeeEvent.eventListener;

import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.event.employeeEvent.EmployeeSalaryChanged;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeePaymentLog;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class EmployeeSalaryChangedEventListener {
    private final EmployeeService employeeService;
    private final EmployeePaymentLogService employeePaymentLogService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;
    private final DepartmentInfoService departmentInfoService;

    @Autowired
    public EmployeeSalaryChangedEventListener(EmployeeService employeeService,
                                              EmployeePaymentLogService employeePaymentLogService,
                                              DepartmentInfoPaymentLogService departmentInfoPaymentLogService,
                                              DepartmentInfoService departmentInfoService) {
        this.employeeService = employeeService;
        this.employeePaymentLogService = employeePaymentLogService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.departmentInfoService = departmentInfoService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEmployeeSalaryChangedEvent(EmployeeSalaryChanged event) {
        log.info("Processing salary changes");
        if (isPositiveSalaryChanges(event))
            processPositiveSalaryChanges(event);
        else
            processNegativeSalaryChanges(event);
    }

    private boolean isPositiveSalaryChanges(EmployeeSalaryChanged event) {
        int compareResult = Money.compareTo(event.getNewSalary(), event.getOldSalary());
        return compareResult == 1;
    }

    private void processNegativeSalaryChanges(EmployeeSalaryChanged event) {
        log.info("PROCESSING NEGATIVE SALARY CHANGES");
        Money salaryChanges = Money.subtract(event.getOldSalary(), event.getNewSalary());
        createEmployeePaymentLog(event.getEmployeeId(), salaryChanges, false);
        if (isSameDepartment(event))
            processPositiveDepartmentBudgetChanges(getDepartmentInfo(event), salaryChanges);
    }

    private void processPositiveDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.employeeSalaryReducing(departmentInfo, budgetChanges);
        createDepartmentInfoPaymentLog(departmentInfo, budgetChanges, true);
    }

    private void processPositiveSalaryChanges(EmployeeSalaryChanged event) {
        log.info("PROCESSING POSITIVE SALARY CHANGES");
        Money salaryChanges = Money.subtract(event.getNewSalary(), event.getOldSalary());
        log.info("Salary changes: {}", salaryChanges.getAmount());
        createEmployeePaymentLog(event.getEmployeeId(), salaryChanges, true);

        if (isSameDepartment(event))
            processNegativeDepartmentBudgetChanges(getDepartmentInfo(event), salaryChanges);
    }

    private void processNegativeDepartmentBudgetChanges(DepartmentInfo departmentInfo, Money budgetChanges) {
        departmentInfoService.allocateBudgetForSalary(departmentInfo, budgetChanges);
        createDepartmentInfoPaymentLog(departmentInfo, Money.abs(budgetChanges), false);
    }

    private void createDepartmentInfoPaymentLog(DepartmentInfo departmentInfo, Money paymentAmount, boolean isPositive) {
        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog
                .createPaymentLog(departmentInfo.getPk(), Money.abs(paymentAmount), isPositive);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void createEmployeePaymentLog(Long employeeId, Money salaryChanges, boolean isPositiveChanges) {
        Employee employee = employeeService.getReference(employeeId);
        EmployeePaymentLog paymentLog =
                EmployeePaymentLog.createPaymentLog(employee, Money.abs(salaryChanges), isPositiveChanges);
        employeePaymentLogService.saveEmployeePaymentLog(paymentLog);
    }

    private DepartmentInfo getDepartmentInfo(EmployeeSalaryChanged event) {
        return departmentInfoService.getById(event.getDepartmentInfoPK());
    }

    private boolean isSameDepartment(EmployeeSalaryChanged event) {
        return event.getOldDepartment().getId().equals(event.getDepartmentInfoPK().getDepartment().getId());
    }
}
