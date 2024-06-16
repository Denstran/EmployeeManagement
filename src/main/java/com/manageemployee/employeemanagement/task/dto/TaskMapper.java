package com.manageemployee.employeemanagement.task.dto;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.service.EmployeeService;
import com.manageemployee.employeemanagement.task.model.Task;
import com.manageemployee.employeemanagement.util.dtomapper.AbstractMapperWithSpecificFields;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskMapper extends AbstractMapperWithSpecificFields<Task, TaskDTO> {
    private final EmployeeService employeeService;

    @Autowired
    protected TaskMapper(ModelMapper mapper, EmployeeService employeeService) {
        super(Task.class, TaskDTO.class, mapper);
        this.employeeService = employeeService;
    }

    @Override
    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Task.class, TaskDTO.class)
                .addMappings(m -> {
                    m.skip(TaskDTO::setTaskOwnerId);
                    m.skip(TaskDTO::setTaskGiverId);
                    m.skip(TaskDTO::setEmployeeGiverContacts);
                    m.skip(TaskDTO::setEmployeeOwnerContacts);
                }).setPostConverter(toDtoConverter());

        mapper.createTypeMap(TaskDTO.class, Task.class)
                .addMappings(m -> {
                    m.skip(Task::setTaskOwner);
                    m.skip(Task::setTaskGiver);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFieldsForDto(Task source, TaskDTO destination) {
        log.info("Mapping Task entity: {} to TaskDTO", source);
        Employee employeeOwner = source.getTaskOwner();
        Employee employeeGiver = source.getTaskGiver();

        destination.setTaskOwnerId(employeeOwner.getId());
        destination.setTaskGiverId(employeeGiver.getId());

        String employeeOwnerContacts = employeeOwner.getEmployeeContacts();

        String employeeGiverContacts = employeeGiver.getEmployeeContacts();

        destination.setEmployeeOwnerContacts(employeeOwnerContacts);
        destination.setEmployeeGiverContacts(employeeGiverContacts);
        log.info("Mapping finished: final TaskDTO: {}", destination);
    }

    @Override
    protected void mapSpecificFieldsForEntity(TaskDTO source, Task destination) {
        log.info("Mapping TaskDTO: {} to Task entity", source);
        destination.setTaskOwner(employeeService.getReference(source.getTaskOwnerId()));
        destination.setTaskGiver(employeeService.getReference(source.getTaskGiverId()));
        log.info("Mapping finished: final Task entity: {}", destination);
    }
}
