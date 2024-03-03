package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.CompanyBranchPaymentLogMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.DepartmentInfoPaymentLogMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeePaymentLogMapper;
import com.manageemployee.employeemanagement.dto.CompanyBranchPaymentLogDto;
import com.manageemployee.employeemanagement.dto.DepartmentInfoPaymentLogDto;
import com.manageemployee.employeemanagement.dto.EmployeePaymentLogDto;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.PaymentLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}")
public class PaymentController {
    private final PaymentLogService paymentLogService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    private final CompanyBranchService companyBranchService;
    private final CompanyBranchPaymentLogMapper companyBranchPaymentLogMapper;
    private final DepartmentInfoPaymentLogMapper departmentInfoPaymentLogMapper;
    private final EmployeePaymentLogMapper employeePaymentLogMapper;
    private final String COMPANY_BRANCH_PAYMENTS_VIEW = "payments/companyBranchPayments";
    private final String DEPARTMENT_PAYMENTS_VIEW = "payments/departmentPayments";
    private final String EMPLOYEE_PAYMENTS_VIEW = "payments/employeePayments";

    public PaymentController(PaymentLogService paymentLogService, DepartmentService departmentService,
                             EmployeeService employeeService, CompanyBranchService companyBranchService, CompanyBranchPaymentLogMapper companyBranchPaymentLogMapper,
                             DepartmentInfoPaymentLogMapper departmentInfoPaymentLogMapper, EmployeePaymentLogMapper employeePaymentLogMapper) {
        this.paymentLogService = paymentLogService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.companyBranchService = companyBranchService;
        this.companyBranchPaymentLogMapper = companyBranchPaymentLogMapper;
        this.departmentInfoPaymentLogMapper = departmentInfoPaymentLogMapper;
        this.employeePaymentLogMapper = employeePaymentLogMapper;
    }

    @GetMapping("/payments")
    public String getCompanyBranchPayments(Model model, @PathVariable Long companyBranchId,
                                           @RequestParam(name = "startDate", required = false) String startDate,
                                           @RequestParam(name = "endDate", required = false) String endDate,
                                           @RequestParam(name = "action", required = false) String action) {
        List<CompanyBranchPaymentLogDto> paymentLogs =
                companyBranchPaymentLogMapper.toDtoList(paymentLogService
                        .getCompanyBranchPayments(companyBranchId, startDate, endDate, action));

        model.addAttribute("paymentLogs", paymentLogs);
        return COMPANY_BRANCH_PAYMENTS_VIEW;
    }

    @GetMapping("/departments/{depId}/payments")
    public String getDepartmentPayments(Model model, @PathVariable Long companyBranchId, @PathVariable Long depId,
                                        @RequestParam(name = "startDate", required = false) String startDate,
                                        @RequestParam(name = "endDate", required = false) String endDate,
                                        @RequestParam(name = "action", required = false) String action) {
        List<DepartmentInfoPaymentLogDto> paymentLogs = departmentInfoPaymentLogMapper
                .toDtoList(paymentLogService.getDepartmentPayments(companyBranchId, depId, startDate, endDate, action));

        String departmentName = departmentService.getById(depId).getDepartmentName();
        model.addAttribute("paymentLogs", paymentLogs);
        model.addAttribute("departmentName", departmentName);
        return DEPARTMENT_PAYMENTS_VIEW;
    }

    @GetMapping("/departments/{depId}/employees/{employeeId}/payments")
    public String getEmployeePayments(Model model, @PathVariable Long companyBranchId, @PathVariable Long depId,
                                      @PathVariable Long employeeId,
                                      @RequestParam(name = "startDate", required = false) String startDate,
                                      @RequestParam(name = "endDate", required = false) String endDate,
                                      @RequestParam(name = "action", required = false) String action) {
        List<EmployeePaymentLogDto> paymentLogs = employeePaymentLogMapper
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
        List<EmployeePaymentLogDto> paymentLogs = employeePaymentLogMapper
                .toDtoList(paymentLogService.getAllEmployeesPaymentLogsFormDepartment
                        (companyBranchId, depId, startDate, endDate, action, phoneNumber));

        Department department = departmentService.getById(depId);
        model.addAttribute("departmentName", department.getDepartmentName());
        model.addAttribute("paymentLogs", paymentLogs);
        return EMPLOYEE_PAYMENTS_VIEW;
    }

}
