package com.example.bright_storage.service.base;

import com.example.bright_storage.model.query.BaseQuery;
import com.example.bright_storage.model.support.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrudService<ENTITY, ID> {

    ENTITY create(ENTITY entity);

    List<ENTITY> create(Collection<ENTITY> entities);

    List<ENTITY> listAll();

    List<ENTITY> listByIdsIn(Collection<ID> ids);

    ENTITY getById(ID id);

    ENTITY getNotNullById(ID id);

    List<ENTITY> query(BaseQuery<ENTITY> query);

    List<ENTITY> query(BaseQuery<ENTITY> query, Pageable pageable);

    void update(ENTITY entity);

    void update(Collection<ENTITY> entities);

    void delete(ENTITY entity);

    ENTITY deleteById(ID id);

    void deleteByIdIn(Collection<ID> ids);

    long count();
}
