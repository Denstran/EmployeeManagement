package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {
    private Long id;

    @Valid
    private Name name;

    @NotNull
    @NotBlank(message = "Номер телефона не должен быть пустым")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
            message = "Неверный формат номера!")
    private String phoneNumber;

    @Valid
    @NotNull
    private Address homeAddress;

    @Email(message = "Неверный формат почты!")
    @NotBlank(message = "email не может быть пустым")
    @NotNull
    private String email;

    @Valid
    @NotNull
    private Money salary;

    @Valid
    @NotNull
    private EmployeeStatusDTO employeeStatus;
    private Long departmentId;
}
