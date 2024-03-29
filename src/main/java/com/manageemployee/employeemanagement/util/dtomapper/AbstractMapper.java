package com.manageemployee.employeemanagement.util.dtomapper;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract implementation of Mapper for mapping entities to dto and vice versa
 * @param <E> - entity class
 * @param <D> - dto class
 */
public abstract class AbstractMapper<E, D> implements Mapper<E, D> {
    final protected ModelMapper mapper;

    protected Class<E> entityClass;
    protected Class<D> dtoClass;

    /**
     * Constructor for creating a mapper
     * @param entityClass - class of the entity
     * @param dtoClass - class of the dto
     * @param mapper - ModelMapper from library, used for mapping objects
     */
    protected AbstractMapper(Class<E> entityClass, Class<D> dtoClass, ModelMapper mapper) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.mapper = mapper;
    }

    /**
     * @see Mapper#toEntity(Object)
     * @param dto - object which should be mapped to entity
     * @return entity object
     */
    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    /**
     * @see Mapper#toDto(Object)
     * @param entity - object which should be mapped to dto
     * @return dto object
     */
    @Override
    public D toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }

    /**
     * @see Mapper#toEntityList(List)
     * @param dtoCollection - collection which should be mapped
     * @return entity collection
     */
    @Override
    public List<E> toEntityList(List<D> dtoCollection) {
        if (dtoCollection == null) {
            return null;
        }

        List<E> entityList = new ArrayList<>();
        dtoCollection.forEach(dto -> entityList.add(toEntity(dto)));
        return entityList;
    }

    /**
     * @see Mapper#toDtoList(List)
     * @param entityCollection - collection which should be mapped
     * @return dto collection
     */
    @Override
    public List<D> toDtoList(List<E> entityCollection) {
        if (entityCollection == null) {
            return null;
        }

        List<D> dtoList = new ArrayList<>();
        entityCollection.forEach(e -> dtoList.add(toDto(e)));
        return dtoList;
    }
}
