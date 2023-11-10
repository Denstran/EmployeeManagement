package com.manageemployee.employeemanagement.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Embeddable
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotNull
    @NotBlank(message = "Город не должен быть пустым!")
    @Column(name = "CITY")
    private String city;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @NotNull
    @NotBlank(message = "Улица не может быть пустой!")
    @Column(name = "STREET")
    private String street;

    @NotNull
    @NotBlank(message = "Страна не может быть пустой!")
    @Column(name = "COUNTRY")
    private String country;

    @Override
    public String toString() {
        if (zipCode == null || zipCode.isEmpty())
            return String.format("%s, %s, %s", country, city, street);

        return String.format("%s, %s, %s, %s", country, city, street, zipCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return city.equals(address.city) && zipCode.equals(address.zipCode) && street.equals(address.street)
                && country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, zipCode, street, country);
    }
}
