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
    private String middleName;

    @NotNull
    @NotBlank(message = "Фамилия не должна быть пустой!")
    @Size(min = 2, max = 30, message = "Фамилия может иметь от 2 до 30 символов!")
    @Column(name = "LAST_NAME")
    private String lastName;

    @PrePersist
    @PreUpdate
    private void setDefaultMiddleName(){
        if (middleName.isEmpty() || middleName == null) {
            middleName = "ОТСУТСТВУЕТ";
        }
    }
}
