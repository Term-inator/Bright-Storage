package com.example.bright_storage.service.base;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrudService<ENTITY, ID> {

    ENTITY create(ENTITY entity);

    List<ENTITY> listAll();

    List<ENTITY> listByIdsIn(Collection<ID> ids);

    Optional<ENTITY> getById(ID id);

    ENTITY getNotNullById(ID id);

    ENTITY update(ENTITY entity);

    void delete(ENTITY entity);

    ENTITY deleteById(ID id);

    long deleteByIdIn(Collection<ID> ids);

    long count();
}
