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
    public void add(UserDto dto) {
        try (var con = ConnectionPool.get()) {
            userDao.create(userMapper.toEntity(dto), con);
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
