package com.manageemployee.employeemanagement.util.dtomapper;

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
    protected AbstractMapperWithSpecificFields(Class<E> entityClass, Class<D> dtoClass, ModelMapper mapper) {
        super(entityClass, dtoClass, mapper);
    }


    /**
     * Converter for getting context to inject specific logic for mapping specific fields.
     * This method should be used in setupMapper method for creating specific type maps for model mapper
     * for mapping specific fields when mapping entity to dto.
     * @return converter with specific mapping logic for dto.
     */
    protected Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    /**
     * Converter for getting context to inject specific logic for mapping specific fields.
     * This method should be used in setupMapper method for creating specific type maps for model mapper
     * for mapping specific fields when mapping dto to entity.
     * @return converter with specific mapping logic for entity
     */
    protected Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    /**
     * Setup method for creating specific type maps for model mapper for mapping specific fields.
     * Should be invoked after object initialization.
     */
    public abstract void setupMapper();

    /**
     * Abstract method for implementing logic for mapping specific fields for dto.
     * This method is being used by toDtoConverter method.
     * @param source - entity object, which will be mapped to dto
     * @param destination - dto object
     */
    protected abstract void mapSpecificFieldsForDto(E source, D destination);

    /**
     * Abstract method for implementing logic for mapping specific fields for entity
     * This method is being used by toEntityConverter method.
     * @param source - dto object, which will be mapped to entity
     * @param destination - entity object
     */
    protected abstract void mapSpecificFieldsForEntity(D source, E destination);
}
