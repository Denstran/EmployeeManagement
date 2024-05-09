package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.VacationRequestMapper;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.service.VacationService;
import com.manageemployee.employeemanagement.employee.validation.vacation.VacationValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/myPage/vacations")
public class VacationController {
    private final VacationService vacationService;
    private final EmployeeService employeeService;
    private final VacationRequestMapper vacationRequestMapper;
    private final VacationValidator vacationValidator;

    private static final String CREATE_VACATION_FORM = "vacation/createVacationForm";
    private static final String EMPLOYEE_VACATIONS = "vacation/employeeVacations";
    private static final String UPDATE_VACATION_FORM = "vacation/updateVacationForm";

    @Autowired
    public VacationController(VacationService vacationService,
                              EmployeeService employeeService,
                              VacationRequestMapper vacationRequestMapper, VacationValidator vacationValidator) {
        this.vacationService = vacationService;
        this.employeeService = employeeService;
        this.vacationRequestMapper = vacationRequestMapper;
        this.vacationValidator = vacationValidator;
    }

    @GetMapping
    public String getEmployeeVacations(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Employee employee = employeeService.getByEmail(userDetails.getUsername()).orElseThrow(() ->
                new SecurityException("Bad Credentials"));
        List<VacationRequestDTO> vacationRequestDTOS =
                vacationRequestMapper.toDtoList(vacationService.getEmployeeVacations(employee));
        model.addAttribute("vacationRequestDTOS", vacationRequestDTOS);

        return EMPLOYEE_VACATIONS;
    }

    @GetMapping("/requestVacation")
    public String createVacationForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        Employee employee = employeeService.getByEmail(userDetails.getUsername()).orElseThrow(() ->
                new SecurityException("Bad Credentials"));
        vacationRequestDTO.setEmployeeId(employee.getId());
        model.addAttribute("vacationRequestDTO", vacationRequestDTO);
        log.info("GET-REQUEST VACATION FORM RECEIVED FROM EMPLOYEE: {}", employee);
        return CREATE_VACATION_FORM;
    }

    @PostMapping("/requestVacation")
    public String createVacation(@ModelAttribute("vacationRequestDTO") @Valid VacationRequestDTO vacationRequestDTO,
                                 BindingResult bindingResult) {
        vacationValidator.validate(vacationRequestDTO, bindingResult);
        if (bindingResult.hasErrors())
            return CREATE_VACATION_FORM;

        log.info("POST-REQUEST VACATION REQUEST PROCEEDED SUCCESSFULLY. SAVING VACATION REQUEST: {}",
                vacationRequestDTO);
        vacationService.createRequest(vacationRequestMapper.toEntity(vacationRequestDTO));
        return "redirect:/vacations";
    }

    @GetMapping("/{vacationId}/update")
    public String updateVacationForm(Model model,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable Long vacationId) {
        log.info("GER-REQUEST FOR UPDATING VACATION RECEIVED FOR USER: {}, VACATION ID: {}", userDetails.getUsername(),
                vacationId);
        VacationRequest vacationRequest = vacationService.getVacationById(vacationId);
        processAccessLegality(userDetails, vacationRequest);

        VacationRequestDTO vacationRequestDTO =
                vacationRequestMapper.toDto(vacationService.getVacationById(vacationId));
        log.info("ACCESS GRANTED. VACATION DTO: {}", vacationRequestDTO);
        model.addAttribute("vacationRequestDTO", vacationRequestDTO);
        return UPDATE_VACATION_FORM;
    }

    @PostMapping("/{vacationId}/update")
    public String updateVacation(@ModelAttribute("vacationRequestDTO") @Valid VacationRequestDTO vacationRequestDTO,
                                 BindingResult bindingResult,
                                 @PathVariable Long vacationId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST-REQUEST FOR UPDATING VACATION RECEIVED, VACATION DTO: {}. STARTING VALIDATION", vacationRequestDTO);
        vacationValidator.validate(vacationRequestDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("VALIDATION FAILED");
            return UPDATE_VACATION_FORM;
        }
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);
        processAccessLegality(userDetails, vacationRequest);
        log.info("VALIDATION SUCCESSFUL. UPDATING VACATION");
        vacationService.updateVacation(vacationRequest);
        return "redirect:/myPage/vacations";
    }

    private void processAccessLegality(UserDetails userDetails, VacationRequest vacationRequest) {
        if (isValidAccess(userDetails, vacationRequest)) return;
        log.warn("ILLEGAL ACCESS TO VACATION. SECURITY EXCEPTION");
        throw new SecurityException("Попытка получить доступ к защищённому, либо не существующему ресурсу!");
    }

    private boolean isValidAccess(UserDetails userDetails, VacationRequest vacationRequest) {
        Employee requester = employeeService.getByEmail(userDetails.getUsername()).orElseThrow(() ->
                new SecurityException("Bad Credentials"));
        Employee vacationOwner =  vacationRequest.getEmployee();
        log.info("VALIDATING REQUEST FOR UPDATING VACATION. REQUESTER: {}, OWNER: {}", requester, vacationOwner);
        return isHeadOfDepartment(requester, vacationOwner.getPosition().getDepartment()) ||
                isVacationOwner(requester, vacationOwner);
    }

    private boolean isVacationOwner(Employee requester, Employee vacationOwner) {
        return Objects.equals(requester.getId(), vacationOwner.getId());
    }
    private boolean isHeadOfDepartment(Employee employee, Department department) {
        return Objects.equals(employee.getPosition().getDepartment().getId(), department.getId()) &&
                employee.getPosition().isLeading();
    }
}
