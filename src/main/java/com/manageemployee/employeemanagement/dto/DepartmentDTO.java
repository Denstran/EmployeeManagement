package com.manageemployee.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DepartmentDTO {
    private Long id;

    @NotNull
    @Size(min = 1, max = 255, message = "Название отдела должно быть от 1 до 255 символов!")
    @NotBlank(message = "Название отдела не может быть пустым!")
    private String departmentName;
}
