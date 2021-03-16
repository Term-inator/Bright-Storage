package com.example.bright_storage.repository;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Inject;

public abstract class AbstractRepository<ENTITY, ID> implements BaseRepository<ENTITY, ID>{

    @Inject
    DbManager manager;

    private Class<ENTITY> actualClass;

    @SuppressWarnings("all")
    public AbstractRepository() {
        actualClass = (Class<ENTITY>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public ENTITY save(ENTITY entity) throws DbException {
        manager.saveBindingId(entity);
        return entity;
    }

    @Override
    public void update(ENTITY entity) throws DbException {
        manager.saveOrUpdate(entity);
    }

    @Override
    public List<ENTITY> findAll() throws DbException {
        return manager.findAll(actualClass);
    }

    @Override
    public ENTITY findById(ID id) throws DbException {
        return manager.findById(actualClass, id);
    }

    @Override
    public ENTITY deleteById(ID id) throws DbException {
        ENTITY entity = findById(id);
        if(entity != null){
            manager.deleteById(actualClass, id);
        }
        return entity;
    }
}
