package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.validationgroups.DefaultGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyBranchDTO {
    private Long id;

    @NotNull(groups = DefaultGroup.class)
    @Valid
    private Money budget;

    @NotNull(groups = DefaultGroup.class)
    @Valid
    private Address companyBranchAddress;

    @NotNull(groups = DefaultGroup.class)
    @NotBlank(message = "Номер телефона не должен быть пустым", groups = DefaultGroup.class)
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
            message = "Неверный формат номера!", groups = DefaultGroup.class)
    private String phoneNumber;
}
