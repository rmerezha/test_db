package org.rmerezha.dao;

import lombok.SneakyThrows;
import org.rmerezha.entity.Role;
import org.rmerezha.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class RoleDao implements Dao<Role> {

    private final static String CREATE = """
                    INSERT INTO Role (name, description)
                    VALUES (?, ?);
                    """;

    private final static String READ = """
                    SELECT id,
                           name,
                           description
                    FROM Role
                    WHERE id = ?;
                    """;

    private final static String UPDATE = """
                    UPDATE Role
                    SET name = ?, description = ?
                    WHERE id = ?;
                    """;

    private final static String DELETE = """
                    DELETE FROM Role
                    WHERE id = ?;
                    """;

    @Override
    @SneakyThrows
    public void create(Role entity, Connection connection) {
        try (var ps = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.executeUpdate();

            var gk = ps.getGeneratedKeys();
            if (gk.next()) {
                entity.setId(gk.getInt("id"));
            }
        }
    }

    @Override
    @SneakyThrows
    public Optional<Role> read(int id, Connection connection) {
        try (var ps = connection.prepareStatement(READ)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Role role = null;
            if (rs.next()) {
                role = Role.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .build();
            }
            return Optional.ofNullable(role);
        }
    }

    @Override
    @SneakyThrows
    public boolean update(Role entity, Connection connection) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getId());
            return ps.executeUpdate() != 0;
        }
    }

    @Override
    @SneakyThrows
    public boolean delete(int id, Connection connection) {
        try (var ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() != 0;
        }
    }
}
