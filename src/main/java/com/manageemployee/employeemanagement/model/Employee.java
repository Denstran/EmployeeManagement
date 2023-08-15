package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import com.manageemployee.employeemanagement.model.enumTypes.EEmployeeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
public class Employee {
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
            @AttributeOverride(name = "CITY",
                column = @Column(name = "HOME_CITY")),
            @AttributeOverride(name = "ZIP_CODE",
                column = @Column(name = "HOME_ZIP_CODE")),
            @AttributeOverride(name = "STREET",
                column = @Column(name = "HOME_STREET")),
            @AttributeOverride(name = "COUNTRY",
                column = @Column(name = "HOME_COUNTRY"))
    })
    private Address homeAddress;

    @Email(message = "Неверный формат почты!")
    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "EMPLOYMENT_DATE", updatable = false)
    @CreationTimestamp
    private Date employmentDate;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_STATUS_ID")
    private EmployeeStatus employeeStatus;

    @NotNull
    @Convert(
            converter = MoneyConverter.class
    )
    @Column(name = "SALARY", length = 63)
    private Money salary;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "EMPLOYEE_POSITION",
            joinColumns = {@JoinColumn(name = "EMPLOYEE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "POSITION_ID")}
    )
    private Set<Position> positions = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<PaymentLog> payments = new HashSet<>();

    public void addPayment(PaymentLog paymentLog) {
        this.payments.add(paymentLog);
        paymentLog.setEmployee(this);
    }

    public void remove(PaymentLog paymentLog) {
        this.payments.remove(paymentLog);
        paymentLog.setEmployee(null);
    }

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;
}
