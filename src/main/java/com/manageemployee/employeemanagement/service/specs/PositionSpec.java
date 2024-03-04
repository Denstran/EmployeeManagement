package com.manageemployee.employeemanagement.service.specs;

import com.manageemployee.employeemanagement.model.Department;
import com.manageemployee.employeemanagement.model.Position;
import org.springframework.data.jpa.domain.Specification;

public class PositionSpec {

    public static Specification<Position> isLeading(boolean isLeading) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("leading"), isLeading);
    }

    public static Specification<Position> isEqualToDepartment(Department department) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("department"), department);
    }
}
