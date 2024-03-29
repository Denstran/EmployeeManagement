package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.employee.dto.EmployeePaymentLogDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.EmployeePaymentLogMapper;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/payments")
public class EmployeePaymentLogController {
    private final EmployeePaymentLogService paymentLogService;
    private final EmployeePaymentLogMapper paymentLogMapper;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    private final String EMPLOYEE_PAYMENTS_VIEW = "payments/employeePayments";

    public EmployeePaymentLogController(EmployeePaymentLogService paymentLogService,
                                        EmployeePaymentLogMapper paymentLogMapper, EmployeeService employeeService,
                                        DepartmentService departmentService) {
        this.paymentLogService = paymentLogService;
        this.paymentLogMapper = paymentLogMapper;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/departments/{depId}/employees/{employeeId}/payments")
    public String getEmployeePayments(Model model, @PathVariable Long companyBranchId, @PathVariable Long depId,
                                      @PathVariable Long employeeId,
                                      @RequestParam(name = "startDate", required = false) String startDate,
                                      @RequestParam(name = "endDate", required = false) String endDate,
                                      @RequestParam(name = "action", required = false) String action) {
        List<EmployeePaymentLogDTO> paymentLogs = paymentLogMapper
                .toDtoList(paymentLogService.getEmployeePaymentLog(employeeId, startDate, endDate, action));
        String employeeName = employeeService.getEmployeeNameById(employeeId);
        model.addAttribute("paymentLogs", paymentLogs);
        model.addAttribute("employeeName", employeeName);

        return EMPLOYEE_PAYMENTS_VIEW;
    }

    @GetMapping("/departments/{depId}/employees/payments")
    public String getEveryEmployeePayments(Model model, @PathVariable Long companyBranchId,
                                           @PathVariable Long depId,
                                           @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                                           @RequestParam(name = "startDate", required = false) String startDate,
                                           @RequestParam(name = "endDate", required = false) String endDate,
                                           @RequestParam(name = "action", required = false) String action) {
        List<EmployeePaymentLogDTO> paymentLogs = paymentLogMapper
                .toDtoList(paymentLogService.getAllEmployeesPaymentLogsFormDepartment
                        (companyBranchId, depId, startDate, endDate, action, phoneNumber));

        Department department = departmentService.getById(depId);
        model.addAttribute("departmentName", department.getDepartmentName());
        model.addAttribute("paymentLogs", paymentLogs);
        return EMPLOYEE_PAYMENTS_VIEW;
    }
}
