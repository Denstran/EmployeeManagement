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
import org.springframework.web.bind.annotation.*;

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
    public String createBranchForm(Model model) {
        CompanyBranchDTO companyBranchDTO = new CompanyBranchDTO();

        model.addAttribute("companyBranchDTO", companyBranchDTO);
        return "createOrUpdateCompanyBranch";
    }

    @PostMapping("/new")
    public String createBranch(@ModelAttribute("companyBranchDTO") @Valid CompanyBranchDTO companyBranchDTO,
                                   BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) return "createOrUpdateCompanyBranch";

        session.setAttribute("viewName", "createOrUpdateCompanyBranch");
        session.setAttribute("dtoClass", companyBranchDTO);
        CompanyBranch companyBranchEntity = companyBranchMapper.toEntity(companyBranchDTO);
        CompanyBranch companyBranch = companyBranchService.createCompanyBranch(companyBranchEntity);
        session.removeAttribute("dtoClass");
        session.removeAttribute("viewName");

        return "redirect:/companyBranches";
    }

    @PostMapping("/delete/{id}")
    public String deleteCompanyBranch(@PathVariable("id") Long id) {
        companyBranchService.deleteCompanyBranchById(id);
        return "redirect:/companyBranches";
    }

    @GetMapping("/update/{id}")
    public String updateCompanyBranchForm(@PathVariable("id") Long id, Model model) {
        CompanyBranch companyBranch = companyBranchService.getCompanyBranchById(id);
        System.out.println(companyBranch.getCompanyBranchAddress());
        CompanyBranchDTO companyBranchDTO = companyBranchMapper.toDto(companyBranch);
        boolean isUpdating = true;
        model.addAttribute("companyBranchDTO", companyBranchDTO);
        model.addAttribute("isUpdating", isUpdating);

        return "createOrUpdateCompanyBranch";
    }

    @PostMapping("/update/{id}")
    public String updateCompanyBranch(@ModelAttribute("companyBranchDTO") @Valid CompanyBranchDTO companyBranchDTO,
                                      BindingResult bindingResult,
                                      HttpSession session) {
        if (bindingResult.hasErrors()) return "createOrUpdateCompanyBranch";

        session.setAttribute("viewName", "createOrUpdateCompanyBranch");
        session.setAttribute("dtoClass", companyBranchDTO);
        CompanyBranch companyBranchEntity = companyBranchMapper.toEntity(companyBranchDTO);
        CompanyBranch companyBranch = companyBranchService.updateCompanyBranch(companyBranchEntity);
        session.removeAttribute("dtoClass");
        session.removeAttribute("viewName");

        return "redirect:/companyBranches";
    }
}
