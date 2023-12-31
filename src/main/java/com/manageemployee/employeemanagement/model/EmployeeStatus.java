package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EMPLOYEE_STATUSES")
@NoArgsConstructor
@Getter
@Setter
public class EmployeeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "EMPLOYEE_STATUS", unique = true)
    @Enumerated(value = EnumType.STRING)
    private EEmployeeStatus employeeStatus;

    public EmployeeStatus(Long id, EEmployeeStatus employeeStatus) {
        this.id = id;
        this.employeeStatus = employeeStatus;
    }

}
