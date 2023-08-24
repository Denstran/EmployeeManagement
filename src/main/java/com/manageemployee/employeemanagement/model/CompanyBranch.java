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

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COMPANY_BRANCH")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyBranch {

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
    @AttributeOverrides({
            @AttributeOverride(name = "CITY",
                column = @Column(name = "COMPANY_BRANCH_CITY")),
            @AttributeOverride(name = "zip_code",
                column = @Column(name = "COMPANY_BRANCH_ZIP_CODE")),
            @AttributeOverride(name = "STREET",
                column = @Column(name = "COMPANY_BRANCH_CITY_STREET")),
            @AttributeOverride(name = "COUNTRY",
            column = @Column(name = "COMPANY_BRANCH_COUNTRY"))
    })
    private Address companyBranchAddress;

    @NotNull
    @NotBlank(message = "Номер телефона не должен быть пустым")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
            message = "Неверный формат номера!")
    @Column(name = "COMPANY_BRANCH_PHONE_NUMBER")
    private String phoneNumber;


    @OneToMany(mappedBy = "companyBranch")
    private Set<Department> departments = new HashSet<>();

    public void addDepartment(Department department) {
        this.departments.add(department);
        department.setCompanyBranch(this);
    }
}
