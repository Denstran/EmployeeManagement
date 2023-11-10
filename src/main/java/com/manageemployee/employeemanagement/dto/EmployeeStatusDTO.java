package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeStatusDTO {
    private Long id;

    @NotNull(message = "Статус не должен быть пустым!")
    private EEmployeeStatus employeeStatus;

    @Override
    public String toString() {
        return "id=" + id + " employeeStatus=" + employeeStatus;
    }
}
