package com.manageemployee.employeemanagement.converter.dtoMappers;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMapper<E, D> implements Mapper<E, D> {
    final ModelMapper mapper;

    private Class<E> entityClass;
    private Class<D> dtoClass;

    AbstractMapper(Class<E> entityClass, Class<D> dtoClass, ModelMapper mapper) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.mapper = mapper;
    }

    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }

    @Override
    public List<E> toEntityCollection(List<D> dtoCollection) {
        if (dtoCollection == null) {
            return null;
        }

        List<E> entityList = new ArrayList<>();
        dtoCollection.forEach(dto -> entityList.add(toEntity(dto)));
        return entityList;
    }

    @Override
    public List<D> toDtoCollection(List<E> entityCollection) {
        if (entityCollection == null) {
            return null;
        }

        List<D> dtoList = new ArrayList<>();
        entityCollection.forEach(e -> dtoList.add(toDto(e)));
        return dtoList;
    }
}
