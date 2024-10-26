# Реалізація інформаційного та програмного забезпечення

## SQL-скрипт для створення на початкового наповнення бази даних

```sql

CREATE SCHEMA IF NOT EXISTS db;

CREATE TABLE IF NOT EXISTS db.role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS db.user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    role_id INT NOT NULL REFERENCES db.role (id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS db.media_content (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    description TEXT NOT NULL,
    type VARCHAR(32) NOT NULL,
    file_path VARCHAR(128) NOT NULL UNIQUE,
    user_id INT NOT NULL REFERENCES db.user (id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS db.project (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    user_id INT NOT NULL REFERENCES db.user (id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS db.analysis_task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    status VARCHAR(64) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    user_id INT NOT NULL REFERENCES db.user (id) ON DELETE NO ACTION,
    project_id INT NOT NULL REFERENCES db.project (id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS db.task_content (
    media_content_id INT NOT NULL REFERENCES db.media_content (id) ON DELETE NO ACTION,
    analysis_task_id INT NOT NULL REFERENCES db.analysis_task (id) ON DELETE NO ACTION,
    PRIMARY KEY (media_content_id, analysis_task_id)
);

CREATE TABLE IF NOT EXISTS db.report (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    analysis_task_id INT NOT NULL REFERENCES db.analysis_task (id) ON DELETE NO ACTION
);

INSERT INTO db.role (name, description)
VALUES ('User Role', 'description for User Role'),
       ('Tech Expert Role', 'description for Tech Expert Role'),
       ('Media Content Analyst Role', 'description for Media Content Analyst Role');

INSERT INTO db.user (name, email, password, role_id)
VALUES
    ('Mock user1', 'test1@test.com', '$2a$12$v.HK88e3zeXbJdQptStutOTyFBitIOdSlOxIfNeOcPey/ZKjtaWPm', 1),
    ('Mock user2', 'test2@test.com', '$2a$12$6IpiXsVmylfNzPBD29YbU.bchJr9IztZpYD/A9PrwUuIn4jEFQEd2', 1),
    ('Mock user3', 'test3@test.com', '$2a$12$363l0yY4Cxy3Gj.hr7D85OJmf0qkvd.tc0VIBxn4svkLazvsbBo3S', 1),
    ('Mock user4', 'test4@test.com', '$2a$12$auMQTmDv9D7lISYaCd7ZQO3VSXUbcjQQrcxdiooQO0EJY3q2bY4hW', 1),
    ('Mock user5', 'test5@test.com', '$2a$12$nRJp1Ad6rRHfzD7l5WLKk.c7EZ/FSuADv0MsM1qYnHSE4YxcG4joO', 1);

INSERT INTO db.media_content (title, description, type, file_path, user_id)
VALUES
    ('media content 1', '...', 'jpg', 'path/to/media/content/1', 1),
    ('media content 2', '...', 'png', 'path/to/media/content/2', 2),
    ('media content 3', '...', 'pdf', 'path/to/media/content/3', 3),
    ('media content 4', '...', 'pdf', 'path/to/media/content/4', 4),
    ('media content 5', '...', 'txt', 'path/to/media/content/5', 5);

INSERT INTO db.project (name, description, user_id)
VALUES
    ('Project 1', '...', 1),
    ('Project 2', '...', 2),
    ('Project 3', '...', 3);

INSERT INTO db.analysis_task (name, status, user_id, project_id)
VALUES
    ('analysis task 1', 'pending', 1, 1),
    ('analysis task 2', 'in progress ', 2, 1),
    ('analysis task 3', 'completed', 3, 1),
    ('analysis task 4', 'paused', 1, 2),
    ('analysis task 5', 'cancelled', 1, 3);

INSERT INTO db.task_content (media_content_id, analysis_task_id)
VALUES
    (5,1),
    (2,1),
    (2,2),
    (1,2),
    (1,5);

INSERT INTO db.report (name, content, analysis_task_id)
VALUES
    ('report 1', '...', 1),
    ('report 2', '...', 2),
    ('report 3', '...', 3),
    ('report 4', '...', 4),
    ('report 5', '...', 5);


```

## RESTfull сервіс для управління даними

### User DTO 
```java
package org.rmerezha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserDto(int id, String name, String email, String password, @JsonProperty("role_id") int roleId) {}
```

### Role DTO
```java
package org.rmerezha.dto;

import lombok.Builder;

@Builder
public record RoleDto(int id, String name, String description) {}
```

### User entity

```java
package org.rmerezha.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class User{
    int id;
    String name; String email;
    String password;
    int roleId;
}
```

### Role entity

```java
package org.rmerezha.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Role{
    int id;
    String name;
    String description;
}
```

### Mappers

```java
package org.rmerezha.mapper;

public interface Mapper<D, E> {

    D toDto(E entity);
    E toEntity(D dto);

}
```

```java
package org.rmerezha.mapper;

import org.rmerezha.dto.RoleDto;
import org.rmerezha.entity.Role;

public class RoleMapper implements Mapper<RoleDto, Role> {

    @Override
    public RoleDto toDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }

    @Override
    public Role toEntity(RoleDto roleDto) {
        return new Role(
                roleDto.id(),
                roleDto.name(),
                roleDto.description()
        );
    }
}

```

```java
package org.rmerezha.mapper;

import org.rmerezha.dto.UserDto;
import org.rmerezha.entity.User;

public class UserMapper implements Mapper<UserDto, User> {

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoleId()
        );
    }

    @Override
    public User toEntity(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email(),
                userDto.password(),
                userDto.roleId()
        );
    }

}
```
### Properties Util

```java
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static final Properties properties = new Properties();

    private static final String PROPERTIES_PATH = "application.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        var resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
        try (resourceAsStream) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

}
```

### Connection Pool for database

```java
package org.rmerezha.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionPool {

    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWD_KEY = "db.passwd";
    private static final ArrayList<Connection> sourceConnections = new ArrayList<>();

    private static ArrayBlockingQueue<Connection> pool;

    static {
        loadDriver();
        initPool();
    }

    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initPool() {
        int poolSize = Integer.parseInt(PropertiesUtil.get(POOL_SIZE_KEY));
        pool = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            var sourceConnection = createConnection();
            var proxyConnection = (Connection)
                    Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? pool.add((Connection) proxy)
                                    : method.invoke(sourceConnection, args));
            pool.add(proxyConnection);
            sourceConnections.add(sourceConnection);
        }
    }

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USER_KEY),
                    PropertiesUtil.get(PASSWD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
```

### Util for JSON

```java
package org.rmerezha.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class JsonBuilder {

    private final static String STATUS_KEY = "status";
    private final static String MESSAGE_KEY = "message";
    private final static String CODE_KEY = "code";
    private final static String DATA_KEY = "data";
    private final static String ERRORS_KEY = "errors";
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectNode rootNode = mapper.createObjectNode();
    private final ObjectNode dataNode = mapper.createObjectNode();

    public JsonBuilder() {
        mapper.registerModule(new JavaTimeModule());
    }

    public JsonBuilder setStatus(Status status) {
        rootNode.put(STATUS_KEY, status.getType());
        return this;
    }

    public JsonBuilder setData(String key, String val) {
        dataNode.put(key, val);
        return this;
    }

    public JsonBuilder setData(String key, long val) {
        dataNode.put(key, val);
        return this;
    }

    public JsonBuilder setData(String key, List<?> vals) {
        JsonNode listNode = mapper.valueToTree(vals);
        dataNode.set(key, listNode);
        return this;
    }

    public JsonBuilder setMessage(String message) {
        rootNode.put(MESSAGE_KEY, message);
        return this;
    }

    public JsonBuilder setErrors(List<Error> errors) {
        ArrayNode errorsNode = mapper.createArrayNode();
        errors.forEach(e -> {
            ObjectNode error = mapper.createObjectNode();
            JsonNode errorCode = mapper.valueToTree(e.getCode());
            JsonNode errorMessage = mapper.valueToTree(e.getMessage());
            error.set(CODE_KEY, errorCode);
            error.set(MESSAGE_KEY, errorMessage);
            errorsNode.add(error);
        });
        rootNode.set(ERRORS_KEY, errorsNode);
        return this;
    }

    public String build() {
        if (!dataNode.isEmpty()) {
            rootNode.set(DATA_KEY, dataNode);
        }
        return rootNode.toString();
    }
}
```

```java
package org.rmerezha.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;

public class JsonParser {

    @SneakyThrows
    public <T> T parse(InputStream in, Class<T> clazz) {

        var objMapper = new ObjectMapper();

        return objMapper.readValue(in, clazz);
    }

}
```

```java
package org.rmerezha.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    USER_EXIST(1, "User with this email already exists"),
    USER_NOT_FOUND(3, "User with this id does not exist"),
    ROLE_EXIST(1, "Role with this name already exists"),
    ROLE_NOT_FOUND(3, "Role with this id does not exist");

    private final int code;
    private final String message;

}
```

```java
package org.rmerezha.util;

import lombok.Getter;

@Getter
public enum Status {
    SUCCESS("success"),
    FAIL("fail");

    private final String type;

    Status(String type) {
        this.type = type;
    }

}
```

### DAO layer

```java
package org.rmerezha.dao;

import java.sql.Connection;
import java.util.Optional;

public interface Dao<E> {

    void create(E entity, Connection connection);

    Optional<E> read(int id, Connection connection);

    boolean update(E entity, Connection connection);

    boolean delete(int id, Connection connection);

}
```

```java
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

```

```java
package org.rmerezha.dao;

import lombok.SneakyThrows;
import org.rmerezha.entity.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class RoleDao implements Dao<Role> {

    private final static String CREATE = """
                    INSERT INTO role (name, description)
                    VALUES (?, ?);
                    """;

    private final static String READ = """
                    SELECT id,
                           name,
                           description
                    FROM role
                    WHERE id = ?;
                    """;

    private final static String UPDATE = """
                    UPDATE role
                    SET name = ?, description = ?
                    WHERE id = ?;
                    """;

    private final static String DELETE = """
                    DELETE FROM role
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
                entity.setId(gk.getInt(1));
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
```

### Repository layer

```java
package org.rmerezha.repository;

import java.util.Optional;

public interface Repository<D, K> {

    Optional<D> get(K id);

    int add(D dto);

    boolean update(D dto);

    boolean remove(K id);

}
```

```java
package org.rmerezha.repository;

import lombok.SneakyThrows;
import org.rmerezha.dao.UserDao;
import org.rmerezha.dto.UserDto;
import org.rmerezha.mapper.UserMapper;
import org.rmerezha.util.ConnectionPool;

import java.util.Optional;

public class UserRepository implements Repository<UserDto, Integer> {

    private final UserDao userDao = new UserDao();
    private final UserMapper userMapper = new UserMapper();


    @Override
    @SneakyThrows
    public Optional<UserDto> get(Integer id) {
        try (var con = ConnectionPool.get()) {
            return userDao.read(id, con).map(userMapper::toDto);
        }
    }

    @Override
    @SneakyThrows
    public int add(UserDto dto) {
        try (var con = ConnectionPool.get()) {
            var user = userMapper.toEntity(dto);
            userDao.create(user, con);
            return user.getId();
        }
    }

    @Override
    @SneakyThrows
    public boolean update(UserDto dto) {
        try (var con = ConnectionPool.get()) {
            return userDao.update(userMapper.toEntity(dto), con);
        }
    }

    @Override
    @SneakyThrows
    public boolean remove(Integer id) {
        try (var con = ConnectionPool.get()) {
            return userDao.delete(id, con);
        }
    }
}
```

```java
package org.rmerezha.repository;

import lombok.SneakyThrows;
import org.rmerezha.dao.RoleDao;
import org.rmerezha.dto.RoleDto;
import org.rmerezha.mapper.RoleMapper;
import org.rmerezha.util.ConnectionPool;

import java.util.Optional;

public class RoleRepository implements Repository<RoleDto, Integer> {

    private final RoleDao roleDao = new RoleDao();
    private final RoleMapper roleMapper = new RoleMapper();


    @Override
    @SneakyThrows
    public Optional<RoleDto> get(Integer id) {
        try (var con = ConnectionPool.get()) {
            return roleDao.read(id, con).map(roleMapper::toDto);
        }
    }

    @Override
    @SneakyThrows
    public int add(RoleDto dto) {
        try (var con = ConnectionPool.get()) {
            var role = roleMapper.toEntity(dto);
            roleDao.create(role, con);
            return role.getId();
        }
    }

    @Override
    @SneakyThrows
    public boolean update(RoleDto dto) {
        try (var con = ConnectionPool.get()) {
            return roleDao.update(roleMapper.toEntity(dto), con);
        }
    }

    @Override
    @SneakyThrows
    public boolean remove(Integer id) {
        try (var con = ConnectionPool.get()) {
            return roleDao.delete(id, con);
        }
    }

}
```

### Service layer

```java
package org.rmerezha.service;

import java.io.InputStream;

public interface Service {

    String get(InputStream jsonStream);

    String add(InputStream jsonStream);

    String update(InputStream jsonStream);

    String remove(InputStream jsonStream);

}
```

```java
package org.rmerezha.service;

import org.rmerezha.dto.UserDto;
import org.rmerezha.repository.UserRepository;
import org.rmerezha.util.Error;
import org.rmerezha.util.JsonBuilder;
import org.rmerezha.util.JsonParser;
import org.rmerezha.util.Status;

import java.io.InputStream;
import java.util.List;

public class UserService implements Service {

    private final JsonParser jsonParser = new JsonParser();
    private final UserRepository userRepository = new UserRepository();


    @Override
    public String get(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var userDto = jsonParser.parse(jsonStream, UserDto.class);
        var optUser = userRepository.get(userDto.id());
        if (optUser.isPresent()) {
            var user = optUser.get();
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", user.id())
                    .setData("name", user.name())
                    .setData("email", user.email())
                    .setData("password", user.password())
                    .setData("roleId", user.roleId());
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.USER_NOT_FOUND));
        }
        return jsonBuilder.build();
    }

    @Override
    public String add(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        try {
            var userDto = jsonParser.parse(jsonStream, UserDto.class);
            int id = userRepository.add(userDto);
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", id);
        } catch (Exception e) {
            jsonBuilder.setStatus(Status.FAIL).setErrors(List.of(Error.USER_EXIST));
        }
        return jsonBuilder.build();
    }

    @Override
    public String update(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var userDto = jsonParser.parse(jsonStream, UserDto.class);
        boolean isUpdated = userRepository.update(userDto);
        if (isUpdated) {
            jsonBuilder.setStatus(Status.SUCCESS);
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.USER_NOT_FOUND));
        }
        return jsonBuilder.build();
    }

    @Override
    public String remove(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var userDto = jsonParser.parse(jsonStream, UserDto.class);
        boolean isRemoved = userRepository.remove(userDto.id());
        if (isRemoved) {
            jsonBuilder.setStatus(Status.SUCCESS);
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.USER_NOT_FOUND));
        }
        return jsonBuilder.build();
    }
}
```

```java
package org.rmerezha.service;

import org.rmerezha.dto.RoleDto;
import org.rmerezha.repository.RoleRepository;
import org.rmerezha.util.Error;
import org.rmerezha.util.JsonBuilder;
import org.rmerezha.util.JsonParser;
import org.rmerezha.util.Status;

import java.io.InputStream;
import java.util.List;

public class RoleService implements Service {

    private final JsonParser jsonParser = new JsonParser();
    private final RoleRepository roleRepository = new RoleRepository();

    @Override
    public String get(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
        var optRole = roleRepository.get(roleDto.id());
        if (optRole.isPresent()) {
            var role = optRole.get();
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", role.id())
                    .setData("name", role.name())
                    .setData("description", role.description());
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.ROLE_NOT_FOUND));
        }
        return jsonBuilder.build();
    }

    @Override
    public String add(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        try {
            var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
            int id = roleRepository.add(roleDto);
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", id);
        } catch (Exception e) {
            jsonBuilder.setStatus(Status.FAIL).setErrors(List.of(Error.ROLE_EXIST));
        }
        return jsonBuilder.build();
    }

    @Override
    public String update(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
        boolean isUpdated = roleRepository.update(roleDto);
        if (isUpdated) {
            jsonBuilder.setStatus(Status.SUCCESS);
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.ROLE_NOT_FOUND));
        }
        return jsonBuilder.build();
    }

    @Override
    public String remove(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
        boolean isRemoved = roleRepository.remove(roleDto.id());
        if (isRemoved) {
            jsonBuilder.setStatus(Status.SUCCESS);
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.ROLE_NOT_FOUND));
        }
        return jsonBuilder.build();
    }
}
```

### Servlet layer

```java
package org.rmerezha.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.rmerezha.service.UserService;

import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static final UserService USER_SERVICE = new UserService();


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = USER_SERVICE.update(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = USER_SERVICE.remove(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = USER_SERVICE.add(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = USER_SERVICE.get(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
}
```

```java
package org.rmerezha.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.rmerezha.service.RoleService;

import java.io.IOException;

@WebServlet("/role")
public class RoleServlet extends HttpServlet {

    private static final RoleService ROLE_SERVICE = new RoleService();


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = ROLE_SERVICE.update(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = ROLE_SERVICE.remove(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = ROLE_SERVICE.add(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var json = ROLE_SERVICE.get(req.getInputStream());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
}
```
