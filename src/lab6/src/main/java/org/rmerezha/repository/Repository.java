package org.rmerezha.repository;

import java.util.Optional;

public interface Repository<D, K> {

    Optional<D> get(K id);

    void add(D dto);

    boolean update(D dto);

    boolean remove(K id);

}
