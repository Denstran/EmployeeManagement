package com.manageemployee.employeemanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.Objects;

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

    @Formula("SELECT COUNT(*) FROM EMPLOYEE WHERE EMPLOYEE.POSITION_ID = ID")
    private int amountOfEmployees;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Отдел, для которого предназначена должность не должен быть пустым!")
    @JoinColumn(name = "DEP_ID", nullable = false, foreignKey =  @ForeignKey(name = "FK_POSITION_DEPARTMENT"))
    private Department department;

    @Column(name = "IS_LEADING")
    @NotNull(message = "Значение управленческой должности не может быть пустым")
    private boolean isLeading;

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
