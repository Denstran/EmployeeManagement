package com.manageemployee.employeemanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "POSITIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Название должности не должно быть пустым!")
    @Size(min = 3, message = "Название должности должно быть больше 3 символов!")
    @Column(name = "POSITION_NAME")
    private String positionName;

    @Min(value = 0, message = "Минимальное необходимое количество сотрудников не может быть ниже 0!")
    @Column(name = "REQUIRED_EMPLOYEE_AMOUNT", nullable = false, columnDefinition = "integer default 0")
    private int requiredEmployeeAmount;

    @Formula("SELECT COUNT(*) FROM POSITION_EMPLOYEE WHERE POSITION_EMPLOYEE.POSITION_ID = ID")
    private int amountOfEmployees;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "POSITION_EMPLOYEE",
        joinColumns = @JoinColumn(name = "POSITION_ID", foreignKey =  @ForeignKey(name = "FK_POSITION"),
                nullable = false),
        inverseJoinColumns = @JoinColumn(name = "EMPLOYEE_ID", foreignKey =  @ForeignKey(name = "FK_EMPLOYEE"),
                nullable = false)
    )
    private Set<Employee> employees = new HashSet<>();

    @ManyToOne
    @NotNull(message = "Отдел, для которого предназначена должность не должен быть пустым!")
    @JoinColumn(name = "DEP_ID", nullable = false, foreignKey =  @ForeignKey(name = "FK_POSITION_DEPARTMENT"))
    private Department department;
}
