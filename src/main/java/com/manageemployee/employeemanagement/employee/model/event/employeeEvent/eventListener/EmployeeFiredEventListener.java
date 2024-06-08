package com.manageemployee.employeemanagement.employee.model.event.employeeEvent.eventListener;

import com.manageemployee.employeemanagement.department.model.CompanyBranchDepartmentPK;
import com.manageemployee.employeemanagement.department.model.DepartmentInfo;
import com.manageemployee.employeemanagement.department.model.DepartmentInfoPaymentLog;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeFired;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class EmployeeFiredEventListener {
    private final DepartmentInfoService departmentInfoService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;

    @Autowired
    public EmployeeFiredEventListener(DepartmentInfoService departmentInfoService,
                                      DepartmentInfoPaymentLogService departmentInfoPaymentLogService) {
        this.departmentInfoService = departmentInfoService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void fireEmployeeEventHandler(EmployeeFired employeeFired) {
        processSalaryChanges(employeeFired);
        blockAccount(employeeFired);
    }

    private void processSalaryChanges(EmployeeFired employeeFired) {
        log.info("PROCESSING SALARY CHANGES");

        DepartmentInfo departmentInfo = getDepartmentInfo(employeeFired);
        departmentInfoService.employeeSalaryReducing(departmentInfo, employeeFired.getOldSalary());

        DepartmentInfoPaymentLog paymentLog = DepartmentInfoPaymentLog
                .createPaymentLog(departmentInfo.getPk(), Money.abs(employeeFired.getOldSalary()), true);
        departmentInfoPaymentLogService.saveDepartmentInfoPaymentLog(paymentLog);
    }

    private void blockAccount(EmployeeFired employeeFired) {
        User user = employeeFired.getEmployee().getUser();
        user.setEnabled(false);
        user.clearRoles();
    }

    private DepartmentInfo getDepartmentInfo(EmployeeFired event) {
        Employee employee = event.getEmployee();
        return departmentInfoService.getById(
                new CompanyBranchDepartmentPK(employee.getCompanyBranch(), employee.getPosition().getDepartment())
        );
    }
}
