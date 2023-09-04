package com.manageemployee.employeemanagement.converter.dtoMappers;

import java.util.List;

public interface Mapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntityCollection(List<D> dtoCollection);

    List<D> toDtoCollection(List<E> entityCollection);
}
