package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.CompanyBranchMapper;
import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.util.validators.companyBranchValidators.CompanyBranchValidator;
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
    private final CompanyBranchValidator companyBranchValidator;

    private final String SHOW_VIEW = "companyBranch/companyBranches";
    private final String VIEW_FOR_UPDATE_OR_CREATE = "companyBranch/createOrUpdateCompanyBranch";
    private final String REDIRECT_URL = "redirect:/companyBranches";

    @Autowired
    public CompanyBranchController(CompanyBranchService companyBranchService,
                                   CompanyBranchMapper companyBranchMapper,
                                   CompanyBranchValidator companyBranchValidator) {
        this.companyBranchService = companyBranchService;
        this.companyBranchMapper = companyBranchMapper;
        this.companyBranchValidator = companyBranchValidator;
    }

    @GetMapping
    public String getAllCompanyBranches(Model model){
        List<CompanyBranch> companyBranchesEntities = companyBranchService.getAllCompanyBranches();
        List<CompanyBranchDTO> companyBranches = companyBranchMapper.toDtoList(companyBranchesEntities);


        model.addAttribute("companyBranches", companyBranches);

        return SHOW_VIEW;
    }

    @GetMapping("/new")
    public String createBranchForm(Model model) {
        CompanyBranchDTO companyBranchDTO = new CompanyBranchDTO();

        model.addAttribute("companyBranchDTO", companyBranchDTO);
        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/new")
    public String createBranch(@ModelAttribute("companyBranchDTO") @Valid CompanyBranchDTO companyBranchDTO,
                                   BindingResult bindingResult) {
        companyBranchValidator.validate(companyBranchDTO, bindingResult);
        if (bindingResult.hasErrors()) return VIEW_FOR_UPDATE_OR_CREATE;

        CompanyBranch companyBranchEntity = companyBranchMapper.toEntity(companyBranchDTO);
        CompanyBranch companyBranch = companyBranchService.createCompanyBranch(companyBranchEntity);

        return REDIRECT_URL;
    }

    @PostMapping("/delete/{id}")
    public String deleteCompanyBranch(@PathVariable("id") Long id) {
        companyBranchService.deleteCompanyBranchById(id);
        return REDIRECT_URL;
    }

    @GetMapping("/update/{id}")
    public String updateCompanyBranchForm(@PathVariable("id") Long id, Model model) {
        CompanyBranch companyBranch = companyBranchService.getCompanyBranchById(id);
        CompanyBranchDTO companyBranchDTO = companyBranchMapper.toDto(companyBranch);

        boolean isUpdating = true;
        model.addAttribute("companyBranchDTO", companyBranchDTO);
        model.addAttribute("isUpdating", isUpdating);

        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/update/{id}")
    public String updateCompanyBranch(@ModelAttribute("companyBranchDTO") @Valid CompanyBranchDTO companyBranchDTO,
                                      BindingResult bindingResult,
                                      @RequestParam("isUpdating") boolean isUpdating, Model model) {
        companyBranchValidator.validate(companyBranchDTO, bindingResult);
        if (bindingResult.hasErrors()){
            model.addAttribute("isUpdating", isUpdating);
            return VIEW_FOR_UPDATE_OR_CREATE;
        }

        CompanyBranch companyBranchEntity = companyBranchMapper.toEntity(companyBranchDTO);
        CompanyBranch companyBranch = companyBranchService.updateCompanyBranch(companyBranchEntity);

        return REDIRECT_URL;
    }
}
