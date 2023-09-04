package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.CompanyBranchMapper;
import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/companyBranches")
public class CompanyBranchController {
    private final CompanyBranchService companyBranchService;
    private final CompanyBranchMapper companyBranchMapper;

    @Autowired
    public CompanyBranchController(CompanyBranchService companyBranchService,
                                   CompanyBranchMapper companyBranchMapper) {
        this.companyBranchService = companyBranchService;
        this.companyBranchMapper = companyBranchMapper;
    }

    @GetMapping
    public String getAllCompanyBranches(Model model){
        List<CompanyBranch> companyBranchesEntities = companyBranchService.getAllCompanyBranches();
        List<CompanyBranchDTO> companyBranches = companyBranchMapper.toDtoCollection(companyBranchesEntities);


        model.addAttribute("companyBranches", companyBranches);

        return "companyBranches";
    }

    @GetMapping("/new")
    public String createBranch(Model model) {
        CompanyBranchDTO companyBranchDTO = new CompanyBranchDTO();

        model.addAttribute("companyBranchDTO", companyBranchDTO);
        return "createCompanyBranch";
    }

    @PostMapping("/new")
    public String createBranchPost(@ModelAttribute("companyBranchDTO") @Valid CompanyBranchDTO companyBranchDTO,
                                   BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) return "createCompanyBranch";

        session.setAttribute("companyBranchDTO", companyBranchDTO);
        CompanyBranch companyBranchEntity = companyBranchMapper.toEntity(companyBranchDTO);
        CompanyBranch companyBranch = companyBranchService.createCompanyBranch(companyBranchEntity);
        session.removeAttribute("companyBranchDTO");

        return "redirect:/companyBranches";
    }
}
