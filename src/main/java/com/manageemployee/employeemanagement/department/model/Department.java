package com.manageemployee.employeemanagement.department.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Objects;

@Entity
@Table(name = "DEPARTMENT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Department extends AbstractAggregateRoot<Department> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Название отдела не может быть пустым!")
    @Column(name = "DEPARTMENT_NAME", unique = true)
    private String departmentName;

    @NotNull(message = "Тип отдела не может быть пустым!")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "DEPARTMENT_TYPE")
    private DepartmentType departmentType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
