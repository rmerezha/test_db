package org.rmerezha.mapper;

public interface Mapper<D, E> {

    D toDto(E entity);
    E toEntity(D dto);

}

