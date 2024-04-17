package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.employee.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.employee.dto.SearchEmployeeFilters;
import com.manageemployee.employeemanagement.employee.model.EmployeeStatus;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import com.manageemployee.employeemanagement.util.validationgroups.DefaultGroup;
import com.manageemployee.employeemanagement.util.validationgroups.UpdatingGroup;
import jakarta.servlet.http.HttpSession;
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
    private final String SHOW_EMPLOYEE = "employee/employee";
    private final String CREATE_OR_UPDATE_FORM = "employee/createOrUpdateEmployee";
    private final String REDIRECT_LINK = "redirect:/companyBranches/%s/departments/%s/employees";

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
        String departmentName = controllerFacade.getDepartmentName(depId);
        log.info("FROM: EMPLOYEE_CONTROLLER. RECEIVED FILTERS: {}", filters);

        List<EmployeeDTO> employeeDTOS = controllerFacade.getEmployeeDTOListFiltered(filters);
        List<PositionDTO> positions = controllerFacade.getPositionDTOList(String.valueOf(depId));

        model.addAttribute("employeeDTOS", employeeDTOS);
        model.addAttribute("positions", positions);
        model.addAttribute("departmentName", departmentName);
        model.addAttribute("filters", filters);
        model.addAttribute("statuses", EmployeeStatus.values());
        return SHOW_EMPLOYEE;
    }

    @GetMapping("/new")
    public String createEmployeeForm(Model model, @PathVariable String companyBranchId, @PathVariable String depId,
                                     HttpSession session) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        List<PositionDTO> positionDTOS = controllerFacade.getPositionDTOList(depId);
        model.addAttribute("positionDTOS", positionDTOS);
        model.addAttribute("isUpdating", false);
        model.addAttribute("employeeDTO", employeeDTO);

        session.setAttribute("positionDTOS", positionDTOS);

        return CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("employeeDTO") @Validated(DefaultGroup.class) EmployeeDTO employeeDTO,
                                 BindingResult bindingResult, Model model, HttpSession session,
                                 @PathVariable String companyBranchId, @PathVariable String depId) {
        log.info("FROM EMPLOYEE_CONTROLLER: RECEIVED DTO {} ON CREATION", employeeDTO);
        controllerFacade.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = (List<PositionDTO>) session.getAttribute("positionDTOS");
            model.addAttribute("positionDTOS", positionDTOS);
            return CREATE_OR_UPDATE_FORM;
        }

        employeeDTO.setCompanyBranchId(Long.valueOf(companyBranchId));
        controllerFacade.createEmployee(employeeDTO);
        session.removeAttribute("positionDTOS");
        return String.format(REDIRECT_LINK, companyBranchId, depId);
    }

    @GetMapping("/{employeeId}/update")
    public String updateEmployeeFrom(Model model, HttpSession session,
                                     @PathVariable String companyBranchId,
                                     @PathVariable String depId,
                                     @PathVariable String employeeId) {
        EmployeeDTO employeeDTO = controllerFacade.getEmployeeDTO(employeeId);
        List<EmployeeStatus> employeeStatuses = List.of(EmployeeStatus.values());
        List<PositionDTO> positionDTOS = controllerFacade.getPositionDTOList(depId);
        model.addAttribute("employeeStatuses", employeeStatuses);
        model.addAttribute("positionDTOS", positionDTOS);
        model.addAttribute("isUpdating", true);
        model.addAttribute("employeeDTO", employeeDTO);

        session.setAttribute("positionDTOS", positionDTOS);

        return CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/{employeeId}/update")
    public String updateEmployee(@ModelAttribute("employeeDTO") @Validated({DefaultGroup.class, UpdatingGroup.class})
                                 EmployeeDTO employeeDTO,
                                 BindingResult bindingResult, Model model, HttpSession session,
                                 @PathVariable String depId,
                                 @PathVariable String employeeId,
                                 @PathVariable String companyBranchId) {
        controllerFacade.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<PositionDTO> positionDTOS = (List<PositionDTO>) session.getAttribute("positionDTOS");
            List<EmployeeStatus> employeeStatuses = List.of(EmployeeStatus.values());
            model.addAttribute("positionDTOS", positionDTOS);
            model.addAttribute("employeeStatuses", employeeStatuses);
            model.addAttribute("isUpdating", true);
            return CREATE_OR_UPDATE_FORM;
        }

        controllerFacade.updateEmployee(employeeDTO);
        session.removeAttribute("positionDTOS");
        return String.format(REDIRECT_LINK, companyBranchId, depId);
    }
}