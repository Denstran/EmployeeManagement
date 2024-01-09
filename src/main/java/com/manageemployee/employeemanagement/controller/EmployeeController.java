package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.EmployeeStatusMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.dto.EmployeeDTO;
import com.manageemployee.employeemanagement.dto.EmployeeStatusDTO;
import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.Employee;
import com.manageemployee.employeemanagement.model.EmployeeStatus;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.EmployeeService;
import com.manageemployee.employeemanagement.service.EmployeeStatusService;
import com.manageemployee.employeemanagement.service.MoneyService;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.EmployeeValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ArrayUtils;

import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;
    private final EmployeeStatusService employeeStatusService;
    private final EmployeeStatusMapper employeeStatusMapper;
    private final MoneyService moneyService;
    private final PositionService positionService;
    private final PositionMapper positionMapper;

    private final String REDIRECT_PATH = "redirect:/companyBranches/%d/departments/%d/employees";
    private final String SHOW_EMPLOYEE = "employee/employee";
    private final String VIEW_FOR_UPDATE_OR_CREATE = "employee/createOrUpdateEmployee";

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper,
                              EmployeeValidator employeeValidator, EmployeeStatusService employeeStatusService,
                              EmployeeStatusMapper employeeStatusMapper, MoneyService moneyService,
                              PositionService positionService, PositionMapper positionMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.employeeValidator = employeeValidator;
        this.employeeStatusService = employeeStatusService;
        this.employeeStatusMapper = employeeStatusMapper;
        this.moneyService = moneyService;
        this.positionService = positionService;
        this.positionMapper = positionMapper;
    }

    @GetMapping()
    public String getAllEmployee(@PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId, Model model) {
        List<Employee> employees = employeeService.getAllEmployeesInDepartment(depId);
        List<EmployeeDTO> employeeDTOS = employeeMapper.toDtoList(employees);

        model.addAttribute("employees", employeeDTOS);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);
        return SHOW_EMPLOYEE;
    }

    @GetMapping("/new")
    public String createEmployeeForm(@PathVariable("companyBranchId") Long companyBranchId,
                                     @PathVariable("depId") Long depId, Model model, HttpSession session) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDepartmentId(depId);

        List<PositionDTO> availablePositions = positionMapper.toDtoList(
                positionService.findAvailablePositionsByDepartment(depId));

        session.setAttribute("availablePositions", availablePositions);

        model.addAttribute("availablePositions", availablePositions);
        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);

        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("employeeDTO") @Valid EmployeeDTO employeeDTO,
                                 BindingResult bindingResult,
                                 @RequestParam(name = "selectedPositions", required = false)Long[] selectedPositions,
                                 @PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId, Model model, HttpSession session) {

        List<PositionDTO> availablePositions = (List<PositionDTO>) session.getAttribute("availablePositions");
        model.addAttribute("availablePositions", availablePositions);

        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) return VIEW_FOR_UPDATE_OR_CREATE;

        if (selectedPositions == null || ArrayUtils.isEmpty(selectedPositions)) {
            model.addAttribute("positionsError", "Не выбрана ни одна должность!");
            return VIEW_FOR_UPDATE_OR_CREATE;
        }
        Employee employee = employeeMapper.toEntity(employeeDTO);

        employeeService.saveEmployee(employee, depId, selectedPositions);
        moneyService.handleEmployeeSalaryChanges(employeeDTO, companyBranchId);

        clearSession(session);
        return String.format(REDIRECT_PATH, companyBranchId, depId);
    }

    @GetMapping("/{empId}/update")
    public String updateEmployeeForm(@PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("empId") Long empId, Model model, HttpSession session) {
        Employee employee = employeeService.getEmployeeById(empId);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        List<EmployeeStatus> employeeStatuses = employeeStatusService.getEmployeeStatuses();
        List<EmployeeStatusDTO> employeeStatusDTOS = employeeStatusMapper.toDtoList(employeeStatuses);

        List<PositionDTO> availablePositions = positionMapper.toDtoList(
                positionService.findAvailablePositionsByDepartmentExceptEmployee(depId, empId)
        );

        List<Position> takenPositionsEntities = positionService.findByEmployeeId(empId);
        List<PositionDTO> takenPositions = positionMapper.toDtoList(takenPositionsEntities);

        session.setAttribute("takenPositionsEntities", takenPositionsEntities);
        session.setAttribute("takenPositions", takenPositions);
        session.setAttribute("availablePositions", availablePositions);
        session.setAttribute("employeeStatusDTOS", employeeStatusDTOS);

        model.addAttribute("takenPositions", takenPositions);
        model.addAttribute("availablePositions", availablePositions);
        model.addAttribute("employeeStatusDTOS", employeeStatusDTOS);
        model.addAttribute("employeeDTO", employeeDTO);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);
        model.addAttribute("isUpdating", true);

        return VIEW_FOR_UPDATE_OR_CREATE;
    }

    @PostMapping("/{empId}/update")
    public String updateEmployee(@ModelAttribute("employeeDTO") @Valid EmployeeDTO employeeDTO,
                                 BindingResult bindingResult,
                                 @PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("empId") Long empId, Model model, HttpSession session,
                                 @RequestParam(required = false, name = "selectedPositions")Long[] selectedPositions,
                                 @RequestParam(required = false, name = "positionsForRemoval")Long[] positionsForRemoval) {

        List<PositionDTO> takenPositions = (List<PositionDTO>) session.getAttribute("takenPositions");
        List<EmployeeStatusDTO> employeeStatusDTOS =
                (List<EmployeeStatusDTO>) session.getAttribute("employeeStatusDTOS");
        List<PositionDTO> availablePositions = (List<PositionDTO>) session.getAttribute("availablePositions");
        List<Position> takenPositionsEntities = (List<Position>) session.getAttribute("takenPositionsEntities");

        model.addAttribute("takenPositions", takenPositions);
        model.addAttribute("availablePositions", availablePositions);
        model.addAttribute("employeeStatusDTOS", employeeStatusDTOS);
        model.addAttribute("isUpdating", true);

        if (positionsForRemoval != null && takenPositions.size() == positionsForRemoval.length) {
            model.addAttribute("takenPositionsError", "Убраны все должности!");
            return VIEW_FOR_UPDATE_OR_CREATE;
        }

        employeeValidator.validate(employeeDTO, bindingResult);
        if (bindingResult.hasErrors()) return VIEW_FOR_UPDATE_OR_CREATE;

        moneyService.handleEmployeeSalaryChanges(employeeDTO, companyBranchId);

        Employee employee = employeeMapper.toEntity(employeeDTO);

        employeeService.updateEmployee(employee, selectedPositions, positionsForRemoval, takenPositionsEntities);
        clearSession(session);
        return String.format(REDIRECT_PATH, companyBranchId, depId);
    }

    @PostMapping("/{empId}/delete")
    public String deleteEmployee(@PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("empId") Long empId) {

        employeeService.deleteEmployeeById(empId, companyBranchId);
        return String.format(REDIRECT_PATH, companyBranchId, depId);
    }

    public void clearSession(HttpSession session) {
        Iterator<String> sessionIterable = session.getAttributeNames().asIterator();
        while (sessionIterable.hasNext())
            session.removeAttribute(sessionIterable.next());
    }

}
