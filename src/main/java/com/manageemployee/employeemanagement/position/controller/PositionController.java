package com.manageemployee.employeemanagement.position.controller;

import com.manageemployee.employeemanagement.department.dto.DepartmentDTO;
import com.manageemployee.employeemanagement.position.dto.PositionDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/positions")
public class PositionController {
    private final PositionControllerFacade controllerFacade;
    private final String SHOW_POSITIONS = "position/positions";
    private final String CREATE_OR_UPDATE_POSITION_FORM = "position/createOrUpdatePosition";
    private final String REDIRECT_LINK = "redirect:/positions";

    @Autowired
    public PositionController(PositionControllerFacade controllerFacade) {
        this.controllerFacade = controllerFacade;
    }

    @GetMapping
    public String getAllPositions(Model model, @RequestParam(value = "department", required = false) String department,
                                  @RequestParam(value = "isLeading", required = false, defaultValue = "all") String isLeading) {
        List<PositionDTO> positionDTOS = controllerFacade.getPositionDTOList(department, isLeading);
        List<DepartmentDTO> departments = controllerFacade.getDepartmentDTOList();
        model.addAttribute("positions", positionDTOS);
        model.addAttribute("departments", departments);

        return SHOW_POSITIONS;
    }

    @GetMapping("/createPosition")
    public String createPositionForm(Model model, HttpSession session) {
        PositionDTO position = new PositionDTO();
        List<DepartmentDTO> departments = controllerFacade.getDepartmentDTOList();

        model.addAttribute("position", position);
        model.addAttribute("departments", departments);
        model.addAttribute("isUpdating", false);
        session.setAttribute("departments", departments);

        return CREATE_OR_UPDATE_POSITION_FORM;
    }

    @PostMapping("/createPosition")
    public String createPosition(@ModelAttribute("position") @Valid PositionDTO position, BindingResult bindingResult,
                                 Model model, HttpSession session) {
        controllerFacade.validate(position, bindingResult);
        if (bindingResult.hasErrors()) {
            List<DepartmentDTO> departments = (List<DepartmentDTO>) session.getAttribute("departments");
            model.addAttribute("departments", departments);
            model.addAttribute("isUpdating", false);
            return CREATE_OR_UPDATE_POSITION_FORM;
        }
        controllerFacade.savePosition(position);
        session.removeAttribute("departments");
        return REDIRECT_LINK;
    }

    @GetMapping("/{positionId}/update")
    public String updatePositionForm(Model model, HttpSession session, @PathVariable Long positionId) {
        DepartmentDTO departmentSelected = controllerFacade.getDepartmentDTOFromPosition(positionId);
        PositionDTO positionDTO = controllerFacade.getPositionDTO(positionId);

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
        controllerFacade.validate(position, bindingResult);
        if (bindingResult.hasErrors()) {
            DepartmentDTO departmentSelected = (DepartmentDTO) session.getAttribute("departmentSelected");
            model.addAttribute("departmentSelected", departmentSelected);
            model.addAttribute("isUpdating", true);
            return CREATE_OR_UPDATE_POSITION_FORM;
        }
        controllerFacade.savePosition(position);
        session.removeAttribute("department");
        return REDIRECT_LINK;
    }
}
