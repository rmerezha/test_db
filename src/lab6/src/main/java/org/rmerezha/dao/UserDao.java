package org.rmerezha.dao;

import lombok.SneakyThrows;
import org.rmerezha.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class UserDao implements Dao<User> {

    private final static String CREATE = """
                    INSERT INTO user (name, email, password, role_id)
                    VALUES (?, ?, ?, ?);
                    """;

    private final static String READ = """
                    SELECT id,
                           name,
                           email,
                           password,
                           role_id
                    FROM user
                    WHERE id = ?;
                    """;

    private final static String UPDATE = """
                    UPDATE user
                    SET name = ?, email = ?, password = ?, role_id = ?
                    WHERE id = ?;
                    """;

    private final static String DELETE = """
                    DELETE FROM user
                    WHERE id = ?;
                    """;

    @Override
    @SneakyThrows
    public void create(User entity, Connection connection) {
        try (var ps = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setInt(4, entity.getRoleId());
            ps.executeUpdate();

            var gk = ps.getGeneratedKeys();
            if (gk.next()) {
                entity.setId(gk.getInt(1));
            }
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> read(int id, Connection connection) {
        try (var ps = connection.prepareStatement(READ)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = User.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .roleId(rs.getInt("role_id"))
                        .build();
            }
            return Optional.ofNullable(user);
        }
    }

    @Override
    @SneakyThrows
    public boolean update(User entity, Connection connection) {
        try (var ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setInt(4, entity.getRoleId());
            ps.setInt(5, entity.getId());
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
