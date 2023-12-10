package com.manageemployee.employeemanagement.controller;

import com.manageemployee.employeemanagement.converter.dtoMappers.PositionMapper;
import com.manageemployee.employeemanagement.dto.PositionDTO;
import com.manageemployee.employeemanagement.model.Position;
import com.manageemployee.employeemanagement.service.PositionService;
import com.manageemployee.employeemanagement.util.PositionValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companyBranches/{companyBranchId}/departments/{depId}/positions")
public class PositionController {
    private final PositionService positionService;
    private final PositionValidator positionValidator;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionController(PositionService positionService, PositionValidator positionValidator,
                              PositionMapper positionMapper) {
        this.positionService = positionService;
        this.positionValidator = positionValidator;
        this.positionMapper = positionMapper;
    }

    @GetMapping("")
    public String getPositions(Model model, @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "3") int size,
                               @PathVariable("companyBranchId") Long companyBranchId,
                               @PathVariable("depId") Long depId) {
        Pageable paging = PageRequest.of(page - 1, size);

        Page<Position> pagePositions = positionService.getPositionsByDepartment(paging, depId);

        List<PositionDTO> positionsDTO = positionMapper.toDtoList(pagePositions.getContent());

        model.addAttribute("positionsDTO", positionsDTO);
        model.addAttribute("currentPage", pagePositions.getNumber() + 1);
        model.addAttribute("totalItems", pagePositions.getTotalElements());
        model.addAttribute("totalPages", pagePositions.getTotalPages());
        model.addAttribute("pageSize", size);

        model.addAttribute("companyBranchId", companyBranchId);

        return "position/positions";
    }

    @GetMapping("/new")
    public String createPositionForm(Model model, @PathVariable("companyBranchId") Long companyBranchId,
                                     @PathVariable("depId") Long depId) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setDepartmentId(depId);
        model.addAttribute("position", positionDTO);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);

        return "position/createOrUpdatePosition";
    }

    @PostMapping("/new")
    public String createPosition(@ModelAttribute("position") @Valid PositionDTO positionDTO,
                                 BindingResult bindingResult,
                                 @PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId) {
        positionValidator.validate(positionDTO, bindingResult);
        if (bindingResult.hasErrors()) return "position/createOrUpdatePosition";

        Position position = positionMapper.toEntity(positionDTO);
        positionService.createPosition(position);

        return String.format("redirect:/companyBranches/%d/departments/%d/positions", companyBranchId, depId);
    }

    @GetMapping("/{positionId}/update")
    public String updatePositionForm(Model model, @PathVariable("companyBranchId") Long companyBranchId,
                                     @PathVariable("depId") Long depId,
                                     @PathVariable("positionId") Long positionId) {
        Position position = positionService.getPositionById(positionId);
        PositionDTO positionDTO = positionMapper.toDto(position);

        boolean isUpdating = true;
        model.addAttribute("position", positionDTO);
        model.addAttribute("companyBranchId", companyBranchId);
        model.addAttribute("depId", depId);
        model.addAttribute("positionId", positionId);
        model.addAttribute("isUpdating", isUpdating);

        return "position/createOrUpdatePosition";
    }

    @PostMapping("/{positionId}/update")
    public String updatePosition(@ModelAttribute("position") @Valid PositionDTO positionDTO,
                                 BindingResult bindingResult,
                                 @PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("positionId") Long positionId,
                                 @RequestParam("isUpdating") boolean isUpdating, Model model
                                 ) {
        positionValidator.validate(positionDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("isUpdating", isUpdating);
            return "position/createOrUpdatePosition";
        }

        Position position = positionMapper.toEntity(positionDTO);
        positionService.updatePosition(position);

        return String.format("redirect:/companyBranches/%d/departments/%d/positions", companyBranchId, depId);
    }

    @PostMapping("/{positionId}/delete")
    public String deletePosition(@PathVariable("companyBranchId") Long companyBranchId,
                                 @PathVariable("depId") Long depId,
                                 @PathVariable("positionId") Long positionId) {
        positionService.deletePositionById(positionId);

        return String.format("redirect:/companyBranches/%d/departments/%d/positions", companyBranchId, depId);
    }
}
