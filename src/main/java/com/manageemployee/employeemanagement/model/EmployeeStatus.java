package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class EmployeeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "EMPLOYEE_STATUS")
    @Enumerated(value = EnumType.STRING)
    private EEmployeeStatus employeeStatus;

    @OneToMany(mappedBy = "employeeStatus", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

}
