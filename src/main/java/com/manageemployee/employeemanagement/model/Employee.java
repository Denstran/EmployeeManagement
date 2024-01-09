package com.manageemployee.employeemanagement.model;

import com.manageemployee.employeemanagement.converter.MoneyConverter;
import com.manageemployee.employeemanagement.model.embeddable.Address;
import com.manageemployee.employeemanagement.model.embeddable.Name;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

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
            @AttributeOverride(name = "city",
                column = @Column(name = "HOME_CITY")),
            @AttributeOverride(name = "zipCode",
                column = @Column(name = "HOME_ZIP_CODE")),
            @AttributeOverride(name = "street",
                column = @Column(name = "HOME_STREET")),
            @AttributeOverride(name = "country",
                column = @Column(name = "HOME_COUNTRY"))
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
    @JoinColumn(name = "DEPARTMENT_ID", nullable = false, foreignKey =  @ForeignKey(name = "FK_EMPLOYEE_DEPARTMENT"))
    private Department department;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "EMPLOYEE_POSITION",
            joinColumns = @JoinColumn(name = "EMPLOYEE_ID", foreignKey =  @ForeignKey(name = "FK_EMPLOYEE"),
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "POSITION_ID", foreignKey =  @ForeignKey(name = "FK_POSITION"),
                    nullable = false)
    )
    private Set<Position> positions = new HashSet<>();

    public void addPosition(Position position) {
        this.positions.add(position);
        position.setRequiredEmployeeAmount(position.getRequiredEmployeeAmount() - 1);
    }

    public void addPositions(List<Position> positions) {
        this.positions.addAll(positions);
        positions.forEach(position -> position.setRequiredEmployeeAmount(position.getRequiredEmployeeAmount() - 1));
    }

    public void removePosition(Position position) {
        this.positions.remove(position);
    }
    public void removePositions(List<Position> positions) {
        this.positions.removeAll(positions);
    }

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
}
