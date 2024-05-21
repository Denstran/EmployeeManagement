package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchPaymentLogDto;
import com.manageemployee.employeemanagement.companyBranch.dto.mappers.CompanyBranchPaymentLogMapper;
import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchPaymentLogService;
import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.department.dto.DepartmentInfoPaymentLogDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentInfoPaymentLogMapper;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentMapper;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import com.manageemployee.employeemanagement.employee.dto.EmployeePaymentLogDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.EmployeePaymentLogMapper;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeePaymentLogService;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/myPage/accounting")
public class AccountingController {
    private final EmployeePaymentLogService employeePaymentLogService;
    private final CompanyBranchPaymentLogService companyBranchPaymentLogService;
    private final DepartmentInfoPaymentLogService departmentInfoPaymentLogService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    private final EmployeePaymentLogMapper employeePaymentLogMapper;
    private final CompanyBranchPaymentLogMapper companyBranchPaymentLogMapper;
    private final DepartmentInfoPaymentLogMapper departmentInfoPaymentLogMapper;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public AccountingController(EmployeePaymentLogService employeePaymentLogService,
                                CompanyBranchPaymentLogService companyBranchPaymentLogService,
                                DepartmentInfoPaymentLogService departmentInfoPaymentLogService,
                                EmployeeService employeeService, DepartmentService departmentService,
                                EmployeePaymentLogMapper employeePaymentLogMapper,
                                CompanyBranchPaymentLogMapper companyBranchPaymentLogMapper,
                                DepartmentInfoPaymentLogMapper departmentInfoPaymentLogMapper,
                                DepartmentMapper departmentMapper) {
        this.employeePaymentLogService = employeePaymentLogService;
        this.companyBranchPaymentLogService = companyBranchPaymentLogService;
        this.departmentInfoPaymentLogService = departmentInfoPaymentLogService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.employeePaymentLogMapper = employeePaymentLogMapper;
        this.companyBranchPaymentLogMapper = companyBranchPaymentLogMapper;
        this.departmentInfoPaymentLogMapper = departmentInfoPaymentLogMapper;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping("/employeesPaymentLogs")
    public String getEmployeePayments(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                                      @RequestParam(name = "startDate", required = false) String startDate,
                                      @RequestParam(name = "endDate", required = false) String endDate,
                                      @RequestParam(name = "action", required = false) String action,
                                      @RequestParam(name = "departmentId", required = false) String departmentId) {
        Employee employee = loadFromUser(userDetails);
        CompanyBranch companyBranch = employee.getCompanyBranch();
        List<EmployeePaymentLogDTO> employeePaymentLogDTOS = employeePaymentLogMapper.toDtoList(
                employeePaymentLogService.getAllEmployeesPaymentLogsFromCompanyBranch
                        (companyBranch, departmentId, startDate, endDate, action, phoneNumber));
        List<DepartmentDTO> departmentDTOS = departmentMapper.toDtoList(departmentService.getAllDepartments());

        model.addAttribute("employeePaymentLogDTOS", employeePaymentLogDTOS);
        model.addAttribute("departmentDTOS", departmentDTOS);
        return "payments/employeePayments";
    }

    @GetMapping("/companyBranchPaymentLogs")
    public String getCompanyBranchPayments(Model model,
                                           @AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam(name = "startDate", required = false) String startDate,
                                           @RequestParam(name = "endDate", required = false) String endDate,
                                           @RequestParam(name = "action", required = false) String action) {
        Employee employee = loadFromUser(userDetails);
        CompanyBranch companyBranch = employee.getCompanyBranch();
        List<CompanyBranchPaymentLogDto> paymentLogs = companyBranchPaymentLogMapper.toDtoList(
                companyBranchPaymentLogService.getCompanyBranchPayments(companyBranch.getId(), startDate, endDate, action)
        );

        model.addAttribute("paymentLogs", paymentLogs);
        return "payments/companyBranchPayments";
    }

    @GetMapping("/departmentsPayments")
    public String getDepartmentsPayments(Model model,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam(name = "startDate", required = false) String startDate,
                                         @RequestParam(name = "endDate", required = false) String endDate,
                                         @RequestParam(name = "action", required = false) String action,
                                         @RequestParam(name = "depId", required = false) String depId) {
        Employee employee = loadFromUser(userDetails);
        Long companyBranchId = employee.getCompanyBranch().getId();
        List<DepartmentInfoPaymentLogDTO> paymentLogs = departmentInfoPaymentLogMapper.toDtoList(
                departmentInfoPaymentLogService.getDepartmentPayments(companyBranchId, depId, startDate, endDate, action)
        );
        List<DepartmentDTO> departmentDTOS = departmentMapper.toDtoList(departmentService.getAllDepartments());

        model.addAttribute("paymentLogs", paymentLogs);
        model.addAttribute("departmentDTOS", departmentDTOS);
        return "payments/departmentPayments";
    }

    private Employee loadFromUser(UserDetails userDetails) {
        return employeeService.getByEmail(userDetails.getUsername()).orElseThrow(() ->
                new IllegalArgumentException("Попытка загрузить не существующего работника!"));
    }
}
