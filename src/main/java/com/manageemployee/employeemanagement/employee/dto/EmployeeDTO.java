package com.manageemployee.employeemanagement.employee.dto;

import com.manageemployee.employeemanagement.employee.model.EmployeeStatus;
import com.manageemployee.employeemanagement.employee.model.Name;
import com.manageemployee.employeemanagement.util.Address;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.validationgroups.DefaultGroup;
import com.manageemployee.employeemanagement.util.validationgroups.UpdatingGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {
    private Long id;

    @Valid
    private Name name;

    @NotNull(groups = {DefaultGroup.class})
    @NotBlank(groups = {DefaultGroup.class} , message = "Номер телефона не должен быть пустым")
    @Pattern(groups = {DefaultGroup.class},
            regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
            message = "Неверный формат номера!")
    private String phoneNumber;

    @Valid
    private Address homeAddress;

    @Email(groups = {DefaultGroup.class}, message = "Неверный формат почты!")
    @NotBlank(groups = {DefaultGroup.class}, message = "email не может быть пустым")
    @NotNull(groups = {DefaultGroup.class})
    private String email;

    @NotNull(groups = {DefaultGroup.class}, message = "Зарплата не может быть пустой")
    @Valid
    private Money salary;

    @NotNull(groups = {UpdatingGroup.class}, message = "Статус сотрудника не может быть пустым!")
    private EmployeeStatus employeeStatus;

    @NotNull(groups = {DefaultGroup.class}, message = "Сотрудник должен числиться в филиале!")
    private Long companyBranchId;

    @NotNull(groups = {DefaultGroup.class}, message = "Сотрудник обязан иметь должность!")
    private Long positionId;

    private Date employmentDate;

    private String positionName;
}
