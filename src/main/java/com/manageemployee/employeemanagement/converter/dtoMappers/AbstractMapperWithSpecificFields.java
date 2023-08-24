package com.manageemployee.employeemanagement.converter.dtoMappers;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public abstract class AbstractMapperWithSpecificFields<E, D> extends AbstractMapper<E, D> {
    AbstractMapperWithSpecificFields(Class<E> entityClass, Class<D> dtoClass, ModelMapper mapper) {
        super(entityClass, dtoClass, mapper);
    }


    Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    abstract void mapSpecificFieldsForDto(E source, D destination);

    abstract void mapSpecificFieldsForEntity(D source, E destination);
}
