package com.manageemployee.employeemanagement.dto;

import com.manageemployee.employeemanagement.model.Money;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyBranchDTO {
    private Long id;
    private Money budget;
    private Address companyBranchAddress;
    private String phoneNumber;
    private Set<DepartmentDTO> departments;
}
