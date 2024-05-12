package com.manageemployee.employeemanagement.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Slf4j
public class SearchEmployeeFilters {
    private Long companyBranchId;
    private Long departmentId;
    private String employeeStatus;
    private String positionName;
    private String phoneNumber;
    private String email;
    private Double salaryStart;
    private Double salaryEnd;
    private Integer startAmountOfWorkingYears;
    private Integer endAmountOfWorkingYears;

    public SearchEmployeeFilters(Long companyBranchId, Long departmentId) {
        this.companyBranchId = companyBranchId;
        this.departmentId = departmentId;
    }

    public SearchEmployeeFilters(Long companyBranchId) {
        this.companyBranchId = companyBranchId;
    }

    @Override
    public String toString() {
        return "SearchEmployeeFilters{" +
                "companyBranchId=" + companyBranchId +
                ", departmentId=" + departmentId +
                ", employeeStatus='" + employeeStatus + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", salaryStart='" + salaryStart + '\'' +
                ", salaryEnd='" + salaryEnd + '\'' +
                ", startAmountOfWorkingYears=" + startAmountOfWorkingYears +
                ", endAmountOfWorkingYears=" + endAmountOfWorkingYears +
                '}';
    }
}
