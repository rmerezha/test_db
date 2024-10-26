package org.rmerezha.dao;

import java.sql.Connection;
import java.util.Optional;

public interface Dao<E> {

    void create(E entity, Connection connection);

    Optional<E> read(int id, Connection connection);

    boolean update(E entity, Connection connection);

    boolean delete(int id, Connection connection);

}

