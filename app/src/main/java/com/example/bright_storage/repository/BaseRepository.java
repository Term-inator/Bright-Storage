package com.example.bright_storage.repository;

import org.xutils.ex.DbException;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<ENTITY, ID> {

    ENTITY save(ENTITY entity) throws DbException;

    void update(ENTITY entity) throws DbException;

    List<ENTITY> findAll() throws DbException;

    ENTITY findById(ID id) throws DbException;

    ENTITY deleteById(ID id) throws DbException;
}
