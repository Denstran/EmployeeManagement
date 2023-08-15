package com.manageemployee.employeemanagement.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotNull
    @NotBlank
    @Column(name = "CITY")
    private String city;

    @NotNull
    @NotBlank
    @Column(name = "ZIP_CODE")
    private String zipCode;

    @NotNull
    @NotBlank
    @Column(name = "STREET")
    private String street;

    @NotNull
    @NotBlank
    @Column(name = "COUNTRY")
    private String country;
}
