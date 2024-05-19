package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.dto.SearchEmployeeFilters;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeeStatus;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.util.validationgroups.DefaultGroup;
import com.manageemployee.employeemanagement.util.validationgroups.UpdatingGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/employees")
@Slf4j
public class EmployeeController {

    private final EmployeeControllerFacade controllerFacade;
    private static final String SHOW_EMPLOYEE = "employee/employee";
    private static final String CREATE_OR_UPDATE_FORM = "employee/createOrUpdateEmployee";
    private static final String REDIRECT_LINK = "redirect:/companyBranches/%s/departments/%s/employees";

    @Autowired
    public EmployeeController(EmployeeControllerFacade controllerFacade) {
        this.controllerFacade = controllerFacade;
    }

    @GetMapping
    public String getEmployees(SearchEmployeeFilters filters, Model model,
                               @PathVariable Long companyBranchId, @PathVariable Long depId) {
        if (filters == null)
            filters = new SearchEmployeeFilters(companyBranchId, depId);

        filters.setDepartmentId(depId);
        log.info("FROM: EMPLOYEE_CONTROLLER. RECEIVED FILTERS: {}", filters);

        setupModelForShowingData(model, filters, depId);
        return SHOW_EMPLOYEE;
    }

    private void setupModelForShowingData(Model model, SearchEmployeeFilters filters, Long depId) {
        String departmentName = controllerFacade.getDepartmentName(depId);
        List<EmployeeDTO> employeeDTOS = controllerFacade.getEmployeeDTOListFiltered(filters);
        List<PositionDTO> positions = controllerFacade.getPositionDTOList(String.valueOf(depId));

        model.addAttribute("employeeDTOS", employeeDTOS);
        model.addAttribute("positions", positions);
        model.addAttribute("departmentName", departmentName);
        model.addAttribute("filters", filters);
        model.addAttribute("statuses", EmployeeStatus.values());
    }

    @GetMapping("/new")
    public String createEmployeeForm(Model model, @PathVariable String depId, @PathVariable Long companyBranchId) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        List<PositionDTO> positionDTOS = controllerFacade.getPositionDTOList(depId);
        setupModelForCreatingEmployee(model, employeeDTO, positionDTOS);
        model.addAttribute("companyBranchId", companyBranchId);
        return CREATE_OR_UPDATE_FORM;
    }

    private void setupModelForCreatingEmployee(Model model, EmployeeDTO employeeDTO, List<PositionDTO> positionDTOS) {
        model.addAttribute("positionDTOS", positionDTOS);
        model.addAttribute("isUpdating", false);
        model.addAttribute("employeeDTO", employeeDTO);
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("employeeDTO") @Validated(DefaultGroup.class) EmployeeDTO employeeDTO,
                                 BindingResult bindingResult, Model model,
                                 @PathVariable String companyBranchId, @PathVariable String depId) {
        log.info("FROM EMPLOYEE_CONTROLLER: RECEIVED DTO {} ON CREATION", employeeDTO);
        controllerFacade.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = controllerFacade.getPositionDTOList(depId);
            model.addAttribute("positionDTOS", positionDTOS);
            model.addAttribute("companyBranchId", companyBranchId);
            return CREATE_OR_UPDATE_FORM;
        }

        employeeDTO.setCompanyBranchId(Long.valueOf(companyBranchId));
        controllerFacade.createEmployee(employeeDTO);
        return String.format(REDIRECT_LINK, companyBranchId, depId);
    }

    @GetMapping("/{employeeId}/update")
    public String updateEmployeeFrom(Model model,
                                     @PathVariable String depId,
                                     @PathVariable String employeeId, @PathVariable Long companyBranchId) {
        List<PositionDTO> positionDTOS = controllerFacade.getPositionDTOList(depId);
        EmployeeDTO employeeDTO = controllerFacade.getEmployeeDTO(employeeId);
        setupModelForUpdating(model, positionDTOS, employeeDTO);
        model.addAttribute(companyBranchId);

        return CREATE_OR_UPDATE_FORM;
    }

    private void setupModelForUpdating(Model model, List<PositionDTO> positionDTOS, EmployeeDTO employeeDTO) {
        model.addAttribute("employeeStatuses", List.of(EmployeeStatus.values()));
        model.addAttribute("positionDTOS", positionDTOS);
        model.addAttribute("isUpdating", true);
        model.addAttribute("employeeDTO", employeeDTO);
    }

    @PostMapping("/{employeeId}/update")
    public String updateEmployee(@ModelAttribute("employeeDTO") @Validated({DefaultGroup.class, UpdatingGroup.class})
                                 EmployeeDTO employeeDTO,
                                 BindingResult bindingResult, Model model,
                                 @PathVariable String depId,
                                 @PathVariable String companyBranchId) {
        controllerFacade.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = controllerFacade.getPositionDTOList(depId);
            recoverModelOnUpdatingError(model, positionDTOS);
            return CREATE_OR_UPDATE_FORM;
        }

        controllerFacade.updateEmployee(employeeDTO);
        return String.format(REDIRECT_LINK, companyBranchId, depId);
    }

    private void recoverModelOnUpdatingError(Model model, List<PositionDTO> positionDTOS) {
        model.addAttribute("positionDTOS", positionDTOS);
        model.addAttribute("employeeStatuses", List.of(EmployeeStatus.values()));
        model.addAttribute("isUpdating", true);
    }
}