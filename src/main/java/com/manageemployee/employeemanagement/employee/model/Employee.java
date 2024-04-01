package com.manageemployee.employeemanagement.employee.model;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.employee.model.event.EmployeeFired;
import com.manageemployee.employeemanagement.employee.model.event.EmployeeHired;
import com.manageemployee.employeemanagement.employee.model.event.EmployeeUpdated;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.util.Address;
import com.manageemployee.employeemanagement.util.Money;
import com.manageemployee.employeemanagement.util.converter.MoneyConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "EMPLOYEE")
@Where(clause = "EMPLOYEE_STATUS != 'FIRED'")
@Getter
@Setter
public class Employee extends AbstractAggregateRoot<Employee> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$",
             message = "Неверный формат номера!")
    @NotNull
    @NotBlank(message = "Телефон не может быть пустым!")
    @Column(unique = true, name = "PHONE_NUMBER")
    private String phoneNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city",
                column = @Column(name = "HOME_CITY")),
            @AttributeOverride(name = "zipCode",
                column = @Column(name = "HOME_ZIP_CODE")),
            @AttributeOverride(name = "street",
                column = @Column(name = "HOME_STREET")),
            @AttributeOverride(name = "country",
                column = @Column(name = "HOME_COUNTRY")),
            @AttributeOverride(name = "buildingNumber",
                column = @Column(name = "HOME_BUILDING_NUMBER"))
    })
    private Address homeAddress;

    @Email(message = "Неверный формат почты!")
    @NotNull
    @Column(unique = true, name = "EMAIL")
    private String email;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "EMPLOYMENT_DATE", updatable = false)
    @CreationTimestamp
    private Date employmentDate;

    @Column(name = "EMPLOYEE_STATUS")
    @Enumerated(value = EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @NotNull
    @Convert(
            converter = MoneyConverter.class
    )
    @Column(name = "SALARY", length = 63)
    private Money salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_BRANCH_ID", nullable = false, foreignKey =  @ForeignKey(name = "FK_EMPLOYEE_COMPANY_BRANCH"))
    private CompanyBranch companyBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSITION_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_EMPLOYEE_POSITION"))
    private Position position;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void hireEmployee() {
        this.employeeStatus = EmployeeStatus.WORKING;
        registerEvent(new EmployeeHired(this, salary, salary));
    }

    public void updateEmployee(Employee oldEmployee) {
        registerEvent(new EmployeeUpdated(this, oldEmployee.salary, salary));
    }

    public void fireEmployee(Money salaryFromDB) {
        this.salary = salaryFromDB;
        registerEvent(new EmployeeFired(this, salary, salary));
    }
}
