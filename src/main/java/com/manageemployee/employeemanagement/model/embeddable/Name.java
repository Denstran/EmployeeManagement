package com.manageemployee.employeemanagement.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Name {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов!")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    @NotNull
    private String middleName;

    @NotNull
    @NotBlank(message = "Фамилия не должна быть пустой!")
    @Size(min = 2, max = 30, message = "Фамилия может иметь от 2 до 30 символов!")
    @Column(name = "LAST_NAME")
    private String lastName;

    @PrePersist
    @PreUpdate
    private void setDefaultMiddleName(){
        if (middleName == null || middleName.isEmpty()) {
            middleName = "ОТСУТСТВУЕТ";
        }
    }

    @Override
    public String toString() {
        if (middleName == null || middleName.isEmpty() || middleName.equals("ОТСУТСТВУЕТ"))
            return String.format("%s %s", firstName, lastName);

        return String.format("%s %s %S", firstName, lastName, middleName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name)) return false;
        Name name = (Name) o;
        return firstName.equals(name.firstName) && middleName.equals(name.middleName)
                && lastName.equals(name.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, middleName, lastName);
    }
}
