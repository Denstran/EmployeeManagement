package com.manageemployee.employeemanagement.department.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentInfoPaymentLogDTO;
import com.manageemployee.employeemanagement.department.dto.mappers.DepartmentInfoPaymentLogMapper;
import com.manageemployee.employeemanagement.department.service.DepartmentInfoPaymentLogService;
import com.manageemployee.employeemanagement.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments")
public class DepartmentInfoPaymentLogController {
    private final DepartmentInfoPaymentLogMapper paymentLogMapper;
    private final DepartmentInfoPaymentLogService paymentLogService;
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentInfoPaymentLogController(DepartmentInfoPaymentLogMapper paymentLogMapper,
                                              DepartmentInfoPaymentLogService paymentLogService,
                                              DepartmentService departmentService) {
        this.paymentLogMapper = paymentLogMapper;
        this.paymentLogService = paymentLogService;
        this.departmentService = departmentService;
    }

    @GetMapping("/{depId}/payments")
    public String getDepartmentPayments(Model model, @PathVariable Long companyBranchId, @PathVariable Long depId,
                                        @RequestParam(name = "startDate", required = false) String startDate,
                                        @RequestParam(name = "endDate", required = false) String endDate,
                                        @RequestParam(name = "action", required = false) String action) {
        List<DepartmentInfoPaymentLogDTO> paymentLogs = paymentLogMapper
                .toDtoList(paymentLogService.getDepartmentPayments(companyBranchId, depId, startDate, endDate, action));

        String departmentName = departmentService.getById(depId).getDepartmentName();
        model.addAttribute("paymentLogs", paymentLogs);
        model.addAttribute("departmentName", departmentName);
        return "payments/departmentPayments";
    }
}
