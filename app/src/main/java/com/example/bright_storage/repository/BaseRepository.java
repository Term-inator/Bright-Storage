package com.example.bright_storage.repository;

import com.example.bright_storage.model.query.BaseQuery;
import com.example.bright_storage.model.support.Pageable;

import org.xutils.db.Selector;

import java.util.Collection;
import java.util.List;

public interface BaseRepository<ENTITY, ID> {

    ENTITY save(ENTITY entity);

    List<ENTITY> save(Collection<ENTITY> entities);

    void update(ENTITY entity);

    void update(Collection<ENTITY> entities);

    List<ENTITY> findAll();

    ENTITY findById(ID id);

    List<ENTITY> findByIds(Collection<ID> ids);

    void delete(ENTITY entity);

    void delete(Collection<ENTITY> entities);

    ENTITY deleteById(ID id);

    void deleteByIds(Collection<ID> id);

    List<ENTITY> query(BaseQuery<ENTITY> query);

    List<ENTITY> query(BaseQuery<ENTITY> query, Pageable pageable);

    long count();
}
