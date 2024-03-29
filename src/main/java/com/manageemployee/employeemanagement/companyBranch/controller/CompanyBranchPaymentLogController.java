package com.manageemployee.employeemanagement.companyBranch.controller;

import com.manageemployee.employeemanagement.companyBranch.dto.CompanyBranchPaymentLogDto;
import com.manageemployee.employeemanagement.companyBranch.dto.mappers.CompanyBranchPaymentLogMapper;
import com.manageemployee.employeemanagement.companyBranch.service.CompanyBranchPaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/payments")
public class CompanyBranchPaymentLogController {
    private final CompanyBranchPaymentLogMapper companyBranchPaymentLogMapper;
    private final CompanyBranchPaymentLogService paymentLogService;

    @Autowired
    public CompanyBranchPaymentLogController(CompanyBranchPaymentLogMapper companyBranchPaymentLogMapper,
                                             CompanyBranchPaymentLogService paymentLogService) {
        this.companyBranchPaymentLogMapper = companyBranchPaymentLogMapper;
        this.paymentLogService = paymentLogService;
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
        return "payments/companyBranchPayments";
    }
}
