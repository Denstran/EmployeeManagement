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
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "POSITION")
@Getter
@Setter
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

    @Formula("SELECT COUNT(*) FROM EMPLOYEE_POSITION WHERE EMPLOYEE_POSITION.POSITION_ID = ID")
    private int amountOfEmployees;

    @ManyToMany(mappedBy = "positions")
    private Set<Employee> employees = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Отдел, для которого предназначена должность не должен быть пустым!")
    @JoinColumn(name = "DEP_ID", nullable = false, foreignKey =  @ForeignKey(name = "FK_POSITION_DEPARTMENT"))
    private Department department;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return Objects.equals(id, position.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
