package com.manageemployee.employeemanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "POSITION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Названи должности не должно быть пустым!")
    @Min(value = 3, message = "Название должности должно быть больше 3 символов!")
    @Column(name = "POSITION_NAME", unique = true)
    private String positionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMP_BRANCH_ID", nullable = false)
    private CompanyBranch companyBranch;

    @ManyToOne
    @JoinColumn(name = "DEP_ID", nullable = false)
    private Department department;
}
