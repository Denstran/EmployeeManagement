package com.manageemployee.employeemanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PositionDTO {
    private Long id;

    @NotNull
    @NotBlank(message = "Название должности не должно быть пустым!")
    @Size(min = 3, message = "Название должности должно быть больше 3 символов!")
    private String positionName;
    private int amountOfEmployees;

    @Min(value = 0, message = "Минимальное необходимое количество сотрудников не может быть ниже 0!")
    private int requiredEmployeeAmount;

    @NotNull(message = "Отдел, для которого предназначена должность не должен быть пустым!")
    private Long departmentId;
}
