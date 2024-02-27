package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "COMPANY_BRANCH", uniqueConstraints = {
        @UniqueConstraint(name = "AddressUniqueConstraint", columnNames = {"COMPANY_BRANCH_CITY",
                "COMPANY_BRANCH_ZIP_CODE", "COMPANY_BRANCH_STREET", "COMPANY_BRANCH_COUNTRY",
                "COMPANY_BRANCH_BUILDING_NUMBER"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyBranch{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(
            converter = MoneyConverter.class
    )
    @Column(name = "COMPANY_BRANCH_BUDGET", length = 63)
    private Money budget;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "city",
                column = @Column(name = "COMPANY_BRANCH_CITY")),
            @AttributeOverride(name = "zipCode",
                column = @Column(name = "COMPANY_BRANCH_ZIP_CODE")),
            @AttributeOverride(name = "street",
                column = @Column(name = "COMPANY_BRANCH_STREET")),
            @AttributeOverride(name = "country",
            column = @Column(name = "COMPANY_BRANCH_COUNTRY")),
            @AttributeOverride(name = "buildingNumber",
                    column = @Column(name = "COMPANY_BRANCH_BUILDING_NUMBER"))
    })
    private Address companyBranchAddress;

    @NotNull
    @NotBlank(message = "Номер телефона не должен быть пустым")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
            message = "Неверный формат номера!")
    @Column(name = "COMPANY_BRANCH_PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyBranch that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}