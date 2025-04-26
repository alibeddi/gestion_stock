package com.polytech.gestionstock.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * Utility class for mapping between entities and DTOs
 */
public class ObjectMapperUtils {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
    }

    /**
     * Hide constructor
     */
    private ObjectMapperUtils() {
    }

    /**
     * Map entity to DTO
     *
     * @param entity  source entity object
     * @param dtoClass destination DTO class
     * @param <D>     type of result object
     * @param <E>     type of source entity
     * @return mapped DTO object
     */
    public static <D, E> D mapToDto(final E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    /**
     * Map DTO to entity
     *
     * @param dto         source DTO object
     * @param entityClass destination entity class
     * @param <D>         type of source DTO
     * @param <E>         type of result entity
     * @return mapped entity object
     */
    public static <D, E> E mapToEntity(final D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    /**
     * Map collection of entities to list of DTOs
     *
     * @param entityCollection source entity collection
     * @param dtoClass        destination DTO class
     * @param <D>             type of result DTO
     * @param <E>             type of source entity
     * @return list of mapped DTO objects
     */
    public static <D, E> List<D> mapAll(final Collection<E> entityCollection, Class<D> dtoClass) {
        return entityCollection.stream()
                .map(entity -> mapToDto(entity, dtoClass))
                .collect(Collectors.toList());
    }

    /**
     * Update entity properties from DTO
     *
     * @param sourceDTO     source DTO object
     * @param targetEntity  destination entity object
     * @param <D>           type of source DTO
     * @param <E>           type of target entity
     */
    public static <D, E> void updateEntityFromDto(D sourceDTO, E targetEntity) {
        modelMapper.map(sourceDTO, targetEntity);
    }
} 