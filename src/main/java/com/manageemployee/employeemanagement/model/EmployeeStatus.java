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
@NoArgsConstructor
@Getter
@Setter
public class EmployeeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "EMPLOYEE_STATUS")
    @Enumerated(value = EnumType.STRING)
    private EEmployeeStatus employeeStatus;

    public EmployeeStatus(Long id, EEmployeeStatus employeeStatus) {
        this.id = id;
        this.employeeStatus = employeeStatus;
    }

    @OneToMany(mappedBy = "employeeStatus", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Employee> employees = new HashSet<>();

    private void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setEmployeeStatus(this);
    }

    private void deleteEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setEmployeeStatus(null);
    }
}
