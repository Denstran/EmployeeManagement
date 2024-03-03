package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeDeleted;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeFired;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeHired;
import com.manageemployee.employeemanagement.model.events.employeeEvents.EmployeeUpdated;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "EMPLOYEE")
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

    @BatchSize(size = 4)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_STATUS_ID", nullable = false,
            foreignKey =  @ForeignKey(name = "FK_EMPLOYEE_EMPLOYEE_STATUS"))
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
        this.employeeStatus = new EmployeeStatus(1L, EEmployeeStatus.WORKING);
        registerEvent(new EmployeeHired(this.salary, this.position.getDepartment(), this.companyBranch, this));
    }

    public void updateEmployee(Employee oldEmployee) {
        registerEvent(new EmployeeUpdated(
                this.salary, this.position.getDepartment(), this.companyBranch, oldEmployee.getSalary(), this));
    }

    public void deleteEmployee() {
        registerEvent(new EmployeeDeleted(this.salary, this.position.getDepartment(),
                this.companyBranch, employeeStatus, this));
    }

    public void fireEmployee() {
        registerEvent(new EmployeeFired(this.salary, this.position.getDepartment(), this.companyBranch));
    }
}
