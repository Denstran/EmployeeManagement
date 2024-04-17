package com.manageemployee.employeemanagement.employee.model;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.position.model.Position;
import com.manageemployee.employeemanagement.util.Money;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

import java.util.Date;

@StaticMetamodel(Employee.class)
public class Employee_ {
    public static volatile SingularAttribute<Employee, Long> id;
    public static volatile SingularAttribute<Employee, Money> salary;
    public static volatile SingularAttribute<Employee, String> phoneNumber;
    public static volatile SingularAttribute<Employee, String> email;
    public static volatile SingularAttribute<Employee, CompanyBranch> companyBranch;
    public static volatile SingularAttribute<Employee, Position> position;
    public static volatile SingularAttribute<Employee, EmployeeStatus> employeeStatus;
    public static volatile SingularAttribute<Employee, Date> employmentDate;
}
