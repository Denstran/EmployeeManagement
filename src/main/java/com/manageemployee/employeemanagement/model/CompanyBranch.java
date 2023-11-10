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
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "city",
                column = @Column(name = "COMPANY_BRANCH_CITY")),
            @AttributeOverride(name = "zipCode",
                column = @Column(name = "COMPANY_BRANCH_ZIP_CODE")),
            @AttributeOverride(name = "street",
                column = @Column(name = "COMPANY_BRANCH_STREET")),
            @AttributeOverride(name = "country",
            column = @Column(name = "COMPANY_BRANCH_COUNTRY"))
    })
    private Address companyBranchAddress;

    @NotNull
    @NotBlank(message = "Номер телефона не должен быть пустым")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
            message = "Неверный формат номера!")
    @Column(name = "COMPANY_BRANCH_PHONE_NUMBER", unique = true)
    private String phoneNumber;


    @OneToMany(mappedBy = "companyBranch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Department> departments = new HashSet<>();

    public void addDepartment(Department department) {
        this.departments.add(department);
        department.setCompanyBranch(this);
    }

    public void removeDepartment(Department department) {
        department.setCompanyBranch(null);
        departments.remove(department);
    }
}
