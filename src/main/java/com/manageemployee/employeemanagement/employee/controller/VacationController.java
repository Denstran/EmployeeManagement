package com.manageemployee.employeemanagement.employee.controller;

import com.manageemployee.employeemanagement.employee.dto.VacationRequestDTO;
import com.manageemployee.employeemanagement.employee.dto.mapper.VacationRequestMapper;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.employee.service.VacationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/myPage/vacations")
public class VacationController {
    private final VacationService vacationService;
    private final EmployeeService employeeService;
    private final VacationRequestMapper vacationRequestMapper;

    private static final String CREATE_VACATION_FORM = "vacation/createVacationForm";
    private static final String EMPLOYEE_VACATIONS = "vacation/employeeVacations";

    @Autowired
    public VacationController(VacationService vacationService,
                              EmployeeService employeeService,
                              VacationRequestMapper vacationRequestMapper) {
        this.vacationService = vacationService;
        this.employeeService = employeeService;
        this.vacationRequestMapper = vacationRequestMapper;
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

        return CREATE_VACATION_FORM;
    }

    @PostMapping("/requestVacation")
    public String createVacation(@ModelAttribute("vacationRequestDTO") @Valid VacationRequestDTO vacationRequestDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return CREATE_VACATION_FORM;

        vacationService.createRequest(vacationRequestMapper.toEntity(vacationRequestDTO));
        return "redirect:/vacations";
    }
}
