package com.manageemployee.employeemanagement.converter.dtoMappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

/**
 * AbstractMapper for dto classes with specific fields, such as ID's
 * @param <E> - entity type
 * @param <D> - dto type
 */
public abstract class AbstractMapperWithSpecificFields<E, D> extends AbstractMapper<E, D> {

    /**
     * @see AbstractMapper#AbstractMapper(Class, Class, ModelMapper)
     * @param entityClass - entity class
     * @param dtoClass - dto class
     * @param mapper - ModelMapper from library, used for mapping objects
     */
    AbstractMapperWithSpecificFields(Class<E> entityClass, Class<D> dtoClass, ModelMapper mapper) {
        super(entityClass, dtoClass, mapper);
    }


    /**
     * Converter for getting context to inject specific logic for mapping specific fields
     * @return converter with specific mapping logic for dto
     */
    Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    /**
     * Converter for getting context to inject specific logic for mapping specific fields
     * @return converter with specific mapping logic for entity
     */
    Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    /**
     * Abstract method for implementing logic for mapping specific fields for dto
     * @param source - entity object, which will be mapped to dto
     * @param destination - dto object
     */
    abstract void mapSpecificFieldsForDto(E source, D destination);

    /**
     * Abstract method for implementing logic for mapping specific fields for entity
     * @param source - dto object, which will be mapped to entity
     * @param destination - entity object
     */
    abstract void mapSpecificFieldsForEntity(D source, E destination);
}
