package com.manageemployee.employeemanagement.employee.model;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeFired;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeHired;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeRestored;
import com.manageemployee.employeemanagement.employee.model.event.employeeEvent.EmployeeUpdated;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.security.User;
import com.manageemployee.employeemanagement.security.UserRole;
import com.manageemployee.employeemanagement.util.Address;
import com.manageemployee.employeemanagement.util.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@ToString
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

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "SALARY"))
    @NotNull
    private Money salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_BRANCH_ID", nullable = false, foreignKey =  @ForeignKey(name = "FK_EMPLOYEE_COMPANY_BRANCH"))
    @ToString.Exclude
    private CompanyBranch companyBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSITION_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_EMPLOYEE_POSITION"))
    @ToString.Exclude
    private Position position;

    // TODO: ИЗМЕНИТЬ НА NULLABLE = FALSE
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_EMPLOYEE_USER"))
    @ToString.Exclude
    private User user;

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

    public void hireEmployee(Set<UserRole> roles, String password) {
        this.employeeStatus = EmployeeStatus.WORKING;
        this.user = User.createUser(email, roles, password);
        registerEvent(new EmployeeHired(this, salary, salary));
    }


    public void updateEmployee(Employee oldEmployee) {
        registerEvent(new EmployeeUpdated(this, oldEmployee.salary, salary, oldEmployee.getPosition()));
    }

    public void fireEmployee(Money salaryFromDB) {
        this.salary = new Money(salaryFromDB);
        registerEvent(new EmployeeFired(this, salary, salary));
    }

    public void restore() {
        registerEvent(new EmployeeRestored(this, salary, salary));
    }
}
