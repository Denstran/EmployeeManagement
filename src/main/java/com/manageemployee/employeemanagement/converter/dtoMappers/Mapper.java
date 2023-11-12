package com.manageemployee.employeemanagement.converter.dtoMappers;

import java.util.List;

/**
 * Interface which provides API for mapping entities to dto and vice versa
 * @param <E> - entity class
 * @param <D> - dto class
 */
public interface Mapper<E, D> {

    /**
     * Method for mapping dto object to entity object
     * @param dto - object which should be mapped to entity
     * @return entity object
     */
    E toEntity(D dto);

    /**
     * Method for mapping entity object to dto object
     * @param entity - object which should be mapped to dto
     * @return dto object
     */
    D toDto(E entity);

    /**
     * Method for mapping collection of dto objects to collection of entity objects
     * @param dtoCollection - collection which should be mapped
     * @return collection of entity objects
     */
    List<E> toEntityList(List<D> dtoCollection);

    /**
     * Method for mapping collection of entity objects to collection of dto objects
     * @param entityCollection - collection which should be mapped
     * @return collection of the dto objects
     */
    List<D> toDtoList(List<E> entityCollection);
}
