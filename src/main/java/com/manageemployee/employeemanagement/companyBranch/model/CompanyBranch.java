package com.manageemployee.employeemanagement.companyBranch.model;

import com.manageemployee.employeemanagement.companyBranch.model.events.CompanyBranchCreated;
import com.manageemployee.employeemanagement.companyBranch.model.events.CompanyBranchDeleted;
import com.manageemployee.employeemanagement.companyBranch.model.events.CompanyBranchUpdated;
import com.manageemployee.employeemanagement.util.Address;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.converter.MoneyConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Objects;

@Entity
@Table(name = "COMPANY_BRANCH", uniqueConstraints = {
        @UniqueConstraint(name = "AddressUniqueConstraint", columnNames = {"COMPANY_BRANCH_CITY",
                "COMPANY_BRANCH_ZIP_CODE", "COMPANY_BRANCH_STREET", "COMPANY_BRANCH_COUNTRY",
                "COMPANY_BRANCH_BUILDING_NUMBER"})
})
@SQLDelete(sql = "UPDATE COMPANY_BRANCH SET DELETED = true WHERE id=?")
@Where(clause = "DELETED=false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyBranch extends AbstractAggregateRoot<CompanyBranch> {

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

    @Column(name = "DELETED")
    private boolean deleted = false;

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

    public void deleteCompanyBranch() {
        registerEvent(new CompanyBranchDeleted(this));
    }

    public void updateCompanyBranch(CompanyBranch companyBranchFromDB) {

        registerEvent(new CompanyBranchUpdated(this, companyBranchFromDB.getBudget()));
    }

    public void createCompanyBranch() {
        registerEvent(new CompanyBranchCreated(this));
    }
}