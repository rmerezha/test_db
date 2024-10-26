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
    public void add(RoleDto dto) {
        try (var con = ConnectionPool.get()) {
            roleDao.create(roleMapper.toEntity(dto), con);
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
