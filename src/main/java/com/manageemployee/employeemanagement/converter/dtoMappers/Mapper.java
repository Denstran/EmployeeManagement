package com.manageemployee.employeemanagement.converter.dtoMappers;

public interface Mapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);
}
