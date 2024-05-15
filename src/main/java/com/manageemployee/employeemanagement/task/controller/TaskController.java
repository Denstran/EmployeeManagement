package com.manageemployee.employeemanagement.task.controller;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.task.dto.TaskDTO;
import com.manageemployee.employeemanagement.task.dto.TaskExtensionDeadlineDTO;
import com.manageemployee.employeemanagement.task.dto.TaskMapper;
import com.manageemployee.employeemanagement.task.model.Task;
import com.manageemployee.employeemanagement.task.service.TaskService;
import com.manageemployee.employeemanagement.task.validation.TaskValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/myPage/tasks")
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final EmployeeService employeeService;
    private final TaskMapper taskMapper;
    private final TaskValidator taskValidator;

    @Autowired
    public TaskController(TaskService taskService, EmployeeService employeeService,
                          TaskMapper taskMapper, TaskValidator taskValidator) {
        this.taskService = taskService;
        this.employeeService = employeeService;
        this.taskMapper = taskMapper;
        this.taskValidator = taskValidator;
    }

    @GetMapping
    public String getEmployeeTasks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Employee owner = loadFromUser(userDetails);

        List<TaskDTO> taskDTOS = taskMapper.toDtoList(taskService.getAllTaskByOwner(owner));
        model.addAttribute("taskDTOS", taskDTOS);

        return "task/employeeTask";
    }

    @GetMapping("/headOfDepartment/givenTasks")
    public String getGivenTasks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Employee employee = loadFromUser(userDetails);
        List<TaskDTO> taskDTOS = taskMapper.toDtoList(taskService.getAllGiveTasksByGiver(employee));
        model.addAttribute("taskDTOS", taskDTOS);

        return "task/givenTasks";
    }

    @GetMapping("/headOfDepartment/createTask/{employeeId}")
    public String createTaskForm(Model model,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long employeeId) {
        log.info("GET-REQUEST RECEIVED FOR CREATING TASK FROM USER: {} WITH ROLES {}",
                userDetails,
                userDetails.getAuthorities());

        Employee taskGiver = loadFromUser(userDetails);
        Employee taskReceiver = employeeService.getById(employeeId);
        log.info("TASK GIVER: {}", taskGiver);
        log.info("TASK RECEIVER: {}", taskReceiver);
        validateAccess(taskGiver, taskReceiver);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskGiverId(taskGiver.getId());
        taskDTO.setTaskOwnerId(taskReceiver.getId());
        model.addAttribute("taskDTO", taskDTO);
        return "task/createTask";
    }

    @PostMapping("/headOfDepartment/createTask/{employeeId}")
    public String createTask(@ModelAttribute("taskDTO") @Valid TaskDTO taskDTO,
                             BindingResult bindingResult,
                             @PathVariable Long employeeId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        Employee giver = loadFromUser(userDetails);
        log.info("POST-REQUEST FOR CREATING TASK FROM USER: {} WITH ROLES: {}",
                userDetails,
                userDetails.getAuthorities());

        taskValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> errors = getGlobalErrors(bindingResult);
            model.addAttribute("errors", errors);
            return "task/createTask";
        }
        Employee receiver = employeeService.getById(employeeId);
        validateAccess(giver, receiver);

        taskService.createTask(taskMapper.toEntity(taskDTO));
        return "redirect:/myPage/tasks/headOfDepartment/givenTasks";
    }

    @PostMapping("/{taskId}/finish")
    public String finishMyTask(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        Employee employee = loadFromUser(userDetails);
        if (!isTaskOwner(task, employee))
            throw new SecurityException("Попытка получить доступ к чужой задаче!");

        taskService.finishTask(task);
        return "redirect:/myPage/tasks";
    }

    @PostMapping("/headOfDepartment/{taskId}/approveTask")
    public String approveTask(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        Employee employee = loadFromUser(userDetails);
        if (isNotTaskGiver(task, employee))
            throw new SecurityException("Попытка получить доступ к чужой задаче!");

        taskService.approveTask(task);
        return "redirect:/myPage/tasks/headOfDepartment/givenTasks";
    }

    @PostMapping("/headOfDepartment/{taskId}/disapproveTask")
    public String disapproveTask(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        Employee employee = loadFromUser(userDetails);
        if (isNotTaskGiver(task, employee))
            throw new SecurityException("Попытка получить доступ к чужой задаче!");

        taskService.disapproveTask(task);
        return "redirect:/myPage/tasks/headOfDepartment/givenTasks";
    }

    @PostMapping("/headOfDepartment/{taskId}/cancelTask")
    public String cancelTask(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        Employee employee = loadFromUser(userDetails);
        if (isNotTaskGiver(task, employee))
            throw new SecurityException("Попытка получить доступ к чужой задаче!");

        taskService.cancelTask(task);
        return "redirect:/myPage/tasks/headOfDepartment/givenTasks";
    }

    @GetMapping("/headOfDepartment/{taskId}/extendTask")
    public String extendTaskForm(Model model,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        Employee employee = loadFromUser(userDetails);
        if (isNotTaskGiver(task, employee))
            throw new SecurityException("Попытка получить доступ к чужой задаче!");
        TaskExtensionDeadlineDTO dateExtensionDTO = new TaskExtensionDeadlineDTO();
        dateExtensionDTO.setExtendedDeadline(task.getTaskDeadLine());

        model.addAttribute("dateExtensionDTO", dateExtensionDTO);
        return "task/extendTaskDeadline";
    }

    @PostMapping("/headOfDepartment/{taskId}/extendTask")
    public String extendTask(@PathVariable Long taskId,
                             @ModelAttribute("dateExtensionDTO") @Valid TaskExtensionDeadlineDTO dateExtensionDTO,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) return "task/extendTaskDeadline";
        Employee employee = loadFromUser(userDetails);
        Task task = taskService.getTaskById(taskId);

        if (isNotTaskGiver(task, employee))
            throw new SecurityException("Попытка получить доступ к чужой задаче!");

        task.setTaskDeadLine(dateExtensionDTO.getExtendedDeadline());
        taskService.extendTaskDeadline(task);

        return "redirect:/myPage/tasks/headOfDepartment/givenTasks";
    }

    private boolean isNotTaskGiver(Task task, Employee employee) {
        return !task.getTaskGiver().getId().equals(employee.getId());
    }

    private boolean isTaskOwner(Task task, Employee employee) {
        return task.getTaskOwner().getId().equals(employee.getId());
    }

    private List<String> getGlobalErrors(BindingResult bindingResult) {
        if (!bindingResult.hasGlobalErrors()) return Collections.emptyList();

        return bindingResult.getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }

    private void validateAccess(Employee taskGiver, Employee taskReceiver) {
        if (!isHeadOfCorrectDepartment(taskGiver, taskReceiver))
            throw new SecurityException("Попытка выдать задачу чужому подчинённому!");
    }

    private boolean isHeadOfCorrectDepartment(Employee taskGiver, Employee taskReceiver) {
        Long giverCompanyBranchId = taskGiver.getCompanyBranch().getId();
        Long receiverCompanyBranchId = taskReceiver.getCompanyBranch().getId();

        Long giverDepId = taskGiver.getPosition().getDepartment().getId();
        Long receiverDepId = taskReceiver.getPosition().getDepartment().getId();

        log.info("COMPANY BRANCH ID1: {}, COMPANY BRANCH ID2: {}", giverCompanyBranchId, receiverCompanyBranchId);
        log.info("DEPARTMENT ID1: {}, DEPARTMENT ID2: {}", giverDepId, receiverDepId);

        boolean isCompanyBranchesEqual = giverCompanyBranchId.equals(receiverCompanyBranchId);
        boolean isDepartmentEqual = giverDepId.equals(receiverDepId);

        log.info("COMPANY BRANCHES EQUAL: {} DEPARTMENTS EQUAL: {}", isCompanyBranchesEqual, isDepartmentEqual);
        return isCompanyBranchesEqual && isDepartmentEqual;
    }

    private Employee loadFromUser(UserDetails userDetails) {
        return employeeService.getByEmail(userDetails.getUsername()).orElseThrow(() ->
                new IllegalArgumentException("Загружен не существующий работник!"));
    }
}
