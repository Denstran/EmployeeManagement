package com.manageemployee.employeemanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Название отдела не может быть пустым!")
    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED", insertable = false, updatable = false)
    private Date lastModified;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COMPANY_BRANCH_ID")
    private CompanyBranch companyBranch;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setDepartment(null);
    }

    @PreUpdate
    @PrePersist
    protected void setDefaultLastModified(){
        lastModified = new Date();
    }
}
