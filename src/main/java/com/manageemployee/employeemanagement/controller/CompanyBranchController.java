package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.CompanyBranchMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.DepartmentInfoMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.DepartmentMapper;
import com.manageemployee.employeemanagement.dto.CompanyBranchDTO;
import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.dto.DepartmentInfoDTO;
import com.manageemployee.employeemanagement.model.CompanyBranch;
import com.manageemployee.employeemanagement.model.DepartmentInfo;
import com.manageemployee.employeemanagement.service.CompanyBranchService;
import com.manageemployee.employeemanagement.service.DepartmentInfoService;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.util.validators.companyBranchValidators.CompanyBranchValidator;
import com.manageemployee.employeemanagement.util.validators.departmentInfoValidators.DepartmentInfoValidator;
import com.manageemployee.employeemanagement.validationgroups.DefaultGroup;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches")
public class CompanyBranchController {
    private final CompanyBranchService companyBranchService;
    private final CompanyBranchMapper companyBranchMapper;
    private final CompanyBranchValidator companyBranchValidator;
    private final DepartmentInfoMapper departmentInfoMapper;
    private final DepartmentInfoValidator departmentInfoValidator;
    private final DepartmentInfoService departmentInfoService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final String ADD_DEPARTMENT_FORM = "companyBranch/addDepartmentForm";
    private final String SHOW_VIEW = "companyBranch/companyBranches";
    private final String SHOW_COMPANY_BRANCH_DEPARTMENTS = "companyBranch/companyBranchDepartments";
    private final String VIEW_FOR_UPDATE_OR_CREATE = "companyBranch/createOrUpdateCompanyBranch";
    private final String REDIRECT_URL = "redirect:/companyBranches";
    private final String REDIRECT_URL_DEPARTMENTS = "redirect:/companyBranches/%d/departments";

    @Autowired
    public CompanyBranchController(CompanyBranchService companyBranchService,
                                   CompanyBranchMapper companyBranchMapper,
                                   CompanyBranchValidator companyBranchValidator,
                                   DepartmentInfoMapper departmentInfoMapper,
                                   DepartmentInfoValidator departmentInfoValidator,
                                   DepartmentInfoService departmentInfoService, DepartmentService departmentService,
                                   DepartmentMapper departmentMapper) {
        this.companyBranchService = companyBranchService;
        this.companyBranchMapper = companyBranchMapper;
        this.companyBranchValidator = companyBranchValidator;
        this.departmentInfoMapper = departmentInfoMapper;
        this.departmentInfoValidator = departmentInfoValidator;
        this.departmentInfoService = departmentInfoService;
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
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
    public String createBranch(@ModelAttribute("companyBranchDTO")
                                   @Validated(DefaultGroup.class) CompanyBranchDTO companyBranchDTO,
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
    public String updateCompanyBranch(@ModelAttribute("companyBranchDTO")
                                      @Validated(DefaultGroup.class) CompanyBranchDTO companyBranchDTO,
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

    @GetMapping("/{companyBranchId}/departments")
    public String getDepartments(@PathVariable("companyBranchId") Long companyBranchId, Model model) {
        List<DepartmentInfoDTO> departmentInfoDTOS =
                departmentInfoMapper.toDtoList(companyBranchService.getDepartments(companyBranchId));
        model.addAttribute("departmentInfoDTOS", departmentInfoDTOS);
        model.addAttribute("companyBranchId", companyBranchId);

        return SHOW_COMPANY_BRANCH_DEPARTMENTS;
    }

    @GetMapping("/{companyBranchId}/departments/add")
    public String addDepartmentInfoForm(@PathVariable("companyBranchId") Long companyBranchId, Model model,
                                    HttpSession session) {
        DepartmentInfoDTO dto = new DepartmentInfoDTO();
        List<DepartmentDTO> availableDepartments =
                departmentMapper.toDtoList(departmentService.getAvailableDepartments(companyBranchId));

        model.addAttribute("dto", dto);
        model.addAttribute("availableDepartments", availableDepartments);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("isUpdating", false);
        session.setAttribute("availableDepartments", availableDepartments);

        return ADD_DEPARTMENT_FORM;
    }

    @PostMapping("/{companyBranchId}/departments/add")
    public String addDepartmentInfo(@ModelAttribute("dto") @Validated(DefaultGroup.class) DepartmentInfoDTO dto,
                                    BindingResult bindingResult,
                                Model model, @PathVariable("companyBranchId") Long companyBranchId,
                                HttpSession session) {
        dto.setCompanyBranchId(companyBranchId);
        departmentInfoValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<DepartmentDTO> availableDepartments =
                    (List<DepartmentDTO>) session.getAttribute("availableDepartments");
            model.addAttribute("availableDepartments", availableDepartments);
            model.addAttribute("companyBranchId", companyBranchId);
            model.addAttribute("isUpdating", false);
            return ADD_DEPARTMENT_FORM;
        }

        DepartmentInfo departmentInfo = departmentInfoMapper.toEntity(dto);
        departmentInfoService.create(departmentInfo);

        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }

    @GetMapping("/{companyBranchId}/departments/update")
    public String updateDepartmentInfoForm(@PathVariable Long companyBranchId, @RequestParam("depId") Long depId,
                                           Model model, HttpSession session) {
       DepartmentInfoDTO dto = departmentInfoMapper.toDto(departmentInfoService.getById(companyBranchId, depId));
       List<DepartmentDTO> availableDepartments = departmentMapper.toDtoList(
               departmentService.getAvailableDepartmentsWhenUpdating(companyBranchId, depId)
       );

       model.addAttribute("dto", dto);
       model.addAttribute("availableDepartments", availableDepartments);
       model.addAttribute("companyBranchId", companyBranchId);
       model.addAttribute("isUpdating", true);

       session.setAttribute("availableDepartments", availableDepartments);

       return ADD_DEPARTMENT_FORM;
    }

    @PostMapping("/{companyBranchId}/departments/update")
    public String updateDepartmentInfo(@ModelAttribute("dto") @Validated(DefaultGroup.class) DepartmentInfoDTO dto,
                                       BindingResult bindingResult, @PathVariable Long companyBranchId,
                                       Model model, HttpSession session) {
        departmentInfoValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<DepartmentDTO> availableDepartments =
                    (List<DepartmentDTO>) session.getAttribute("availableDepartments");
            model.addAttribute("availableDepartments", availableDepartments);
            model.addAttribute("companyBranchId", companyBranchId);
            model.addAttribute("isUpdating", true);

            return ADD_DEPARTMENT_FORM;
        }

        departmentInfoService.update(departmentInfoMapper.toEntity(dto));

        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }

    @PostMapping("/{companyBranchId}/departments/{departmentId}/remove")
    public String removeDepartmentInfo(@PathVariable Long companyBranchId, @PathVariable Long departmentId) {
        DepartmentInfo departmentInfo = departmentInfoService.getById(companyBranchId, departmentId);
        departmentInfoService.deleteDepartmentInfo(departmentInfo);
        return String.format(REDIRECT_URL_DEPARTMENTS, companyBranchId);
    }

}
