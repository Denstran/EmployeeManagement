package com.manageemployee.employeemanagement.model.events.departmentEvents;

import com.manageemployee.employeemanagement.model.Department;
import lombok.Data;

@Data
public class DepartmentDeleted {
    private final Department department;

}
