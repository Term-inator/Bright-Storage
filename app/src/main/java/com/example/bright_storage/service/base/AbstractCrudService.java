package com.example.bright_storage.service.base;

import com.example.bright_storage.model.query.BaseQuery;
import com.example.bright_storage.model.support.Pageable;
import com.example.bright_storage.repository.BaseRepository;
import com.example.bright_storage.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCrudService<ENTITY, ID> implements CrudService<ENTITY, ID>{

    private final String entityName;
    protected BaseRepository<ENTITY, ID> repository;

    @SuppressWarnings("all")
    public AbstractCrudService() {
        Class<ENTITY> actualClass = (Class<ENTITY>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityName = actualClass.getSimpleName();
    }

    @Override
    public ENTITY create(ENTITY entity) {
        return repository.save(entity);
    }

    @Override
    public List<ENTITY> listByIdsIn(Collection<ID> ids) {
        return repository.findByIds(ids);
    }

    @Override
    public List<ENTITY> listAll() {
        return repository.findAll();
    }

    @Override
    public ENTITY getById(ID id) {
        return repository.findById(id);
    }

    @Override
    public ENTITY getNotNullById(ID id) {
        ENTITY entity = repository.findById(id);
        Assert.notNull(entity, entityName + "(id: " + id + ") could not be found");
        return entity;
    }

    @Override
    public List<ENTITY> query(BaseQuery<ENTITY> query) {
        return repository.query(query);
    }

    @Override
    public List<ENTITY> query(BaseQuery<ENTITY> query, Pageable pageable) {
        return repository.query(query,pageable);
    }

    @Override
    public void update(ENTITY entity) {
        repository.update(entity);
    }

    @Override
    public void delete(ENTITY entity) {
        if(entity != null){
            repository.delete(entity);
        }
    }

    @Override
    public void deleteByIdIn(Collection<ID> ids) {
        repository.deleteByIds(ids);
    }

    @Override
    public ENTITY deleteById(ID id) {
        return repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
