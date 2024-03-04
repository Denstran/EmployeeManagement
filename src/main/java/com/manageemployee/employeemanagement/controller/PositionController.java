package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.DepartmentMapper;
import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.DepartmentService;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.validators.positionValidators.PositionValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/positions")
public class PositionController {
    private final PositionService positionService;
    private final DepartmentService departmentService;
    private final PositionMapper positionMapper;
    private final PositionValidator positionValidator;
    private final DepartmentMapper departmentMapper;
    private final String SHOW_POSITIONS = "position/positions";
    private final String CREATE_OR_UPDATE_POSITION_FORM = "position/createOrUpdatePosition";
    private final String REDIRECT_LINK = "redirect:/positions";
    @Autowired
    public PositionController(PositionService positionService, DepartmentService departmentService,
                              PositionMapper positionMapper, PositionValidator positionValidator,
                              DepartmentMapper departmentMapper) {
        this.positionService = positionService;
        this.departmentService = departmentService;
        this.positionMapper = positionMapper;
        this.positionValidator = positionValidator;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    public String getAllPositions(Model model, @RequestParam(value = "department", required = false) String department,
                                  @RequestParam(value = "isLeading", required = false, defaultValue = "all") String isLeading) {
        Optional<Department> departmentOptional = departmentService.getByName(department);
        Department d = departmentOptional.orElse(null);

        List<PositionDTO> positionDTOS = positionMapper.toDtoList(positionService.getAllPositions(d, isLeading));
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("positions", positionDTOS);
        model.addAttribute("departments", departments);

        return SHOW_POSITIONS;
    }

    @GetMapping("/departmentPositions/{departmentId}")
    public String getDepartmentPositions(Model model, @PathVariable Long departmentId) {
        List<PositionDTO> positions =
                positionMapper.toDtoList(positionService.getPositionsByDepartmentId(departmentId));
        model.addAttribute("positions", positions);

        return SHOW_POSITIONS;
    }

    @GetMapping("/createPosition")
    public String createPositionForm(Model model, HttpSession session) {
        PositionDTO position = new PositionDTO();
        List<DepartmentDTO> departments = departmentMapper.toDtoList(departmentService.getAllDepartments());

        model.addAttribute("position", position);
        model.addAttribute("departments", departments);
        model.addAttribute("isUpdating", false);
        session.setAttribute("departments", departments);

        return CREATE_OR_UPDATE_POSITION_FORM;
    }

    @PostMapping("/createPosition")
    public String createPosition(@ModelAttribute("position") @Valid PositionDTO position, BindingResult bindingResult,
                                 Model model, HttpSession session) {
        positionValidator.validate(position, bindingResult);
        if (bindingResult.hasErrors()) {
            List<DepartmentDTO> departments = (List<DepartmentDTO>) session.getAttribute("departments");
            model.addAttribute("departments", departments);
            model.addAttribute("isUpdating", false);
            return CREATE_OR_UPDATE_POSITION_FORM;
        }
        positionService.savePosition(positionMapper.toEntity(position));
        session.removeAttribute("departments");
        return REDIRECT_LINK;
    }

    @GetMapping("/{positionId}/update")
    public String updatePositionForm(Model model, HttpSession session, @PathVariable Long positionId) {
        Position position = positionService.getById(positionId);
        DepartmentDTO departmentSelected = departmentMapper.toDto(position.getDepartment());
        PositionDTO positionDTO = positionMapper.toDto(position);

        model.addAttribute("position", positionDTO);
        model.addAttribute("departmentSelected", departmentSelected);
        model.addAttribute("isUpdating", true);
        session.setAttribute("departmentSelected", departmentSelected);

        return CREATE_OR_UPDATE_POSITION_FORM;
    }

    @PostMapping("/{positionId}/update")
    public String updatePosition(@ModelAttribute("position") @Valid PositionDTO position,
                                 BindingResult bindingResult, Model model, HttpSession session,
                                 @PathVariable Long positionId) {
        positionValidator.validate(position, bindingResult);
        if (bindingResult.hasErrors()) {
            DepartmentDTO departmentSelected = (DepartmentDTO) session.getAttribute("departmentSelected");
            model.addAttribute("departmentSelected", departmentSelected);
            model.addAttribute("isUpdating", true);
            return CREATE_OR_UPDATE_POSITION_FORM;
        }
        positionService.savePosition(positionMapper.toEntity(position));
        session.removeAttribute("department");
        return REDIRECT_LINK;
    }

    @PostMapping("/{positionId}/delete")
    public String deletePosition(@PathVariable Long positionId) {
        positionService.deleteById(positionId);
        return REDIRECT_LINK;
    }
}
