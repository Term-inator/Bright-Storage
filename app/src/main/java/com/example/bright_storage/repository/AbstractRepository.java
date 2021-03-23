package com.example.bright_storage.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bright_storage.exception.DBException;
import com.example.bright_storage.model.entity.BaseEntity;
import com.example.bright_storage.model.query.BaseQuery;
import com.example.bright_storage.model.support.Pageable;
import com.example.bright_storage.util.Assert;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public abstract class AbstractRepository<ENTITY extends BaseEntity, ID> implements BaseRepository<ENTITY, ID>{

    @Inject
    DbManager manager;

    private final Class<ENTITY> actualClass;

    private final String entityName;

    @SuppressWarnings("all")
    public AbstractRepository() {
        actualClass = (Class<ENTITY>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        entityName = actualClass.getSimpleName();
    }

    @Override
    public ENTITY save(ENTITY entity){
        Assert.notNull(entity, entityName + " must not be null");
        entity.prePersist();
        try {
            manager.saveBindingId(entity);
            return entity;
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void update(ENTITY entity){
        Assert.notNull(entity, entityName + " must not be null");
        entity.preUpdate();
        try {
            manager.saveOrUpdate(entity);
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public List<ENTITY> findAll(){
        try {
            List<ENTITY> list = manager.findAll(actualClass);
            return list == null ? new ArrayList<>() : list;
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public List<ENTITY> findByIds(Collection<ID> ids) {
        Assert.notNull(ids, "Ids must not be null");
        try {
            Selector<ENTITY> selector = manager.selector(actualClass);
            selector.where(selector.getTable().getId().getName(), "in", ids);
            return selector.findAll();
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public ENTITY findById(ID id){
        Assert.notNull(id, "Id must be null");
        try {
            return manager.findById(actualClass, id);
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public List<ENTITY> query(BaseQuery<ENTITY> query) {
        return query(query, new Pageable());
    }

    @Override
    public List<ENTITY> query(BaseQuery<ENTITY> query, Pageable pageable) {
        Assert.notNull(query, "Query must not be null");
        if(pageable == null){
            pageable = new Pageable();
        }
        Selector<ENTITY> selector = query.toSelector(manager, actualClass);

        selector.orderBy("");
        selector.getOrderByList().clear();
        selector.getOrderByList().addAll(pageable.getOrderByList());

        selector.offset(pageable.getOffset()).limit(pageable.getSize());
        try {
            return selector.findAll();
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void delete(ENTITY entity) {
        Assert.notNull(entity, entityName + " must not be null");
        try {
            manager.delete(entity);
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public ENTITY deleteById(ID id){
        Assert.notNull(id, "Id must not be nul");
        ENTITY entity = findById(id);
        if(entity != null){
            try {
                manager.deleteById(actualClass, id);
            } catch (DbException e) {
                throw new DBException(e.getMessage());
            }
        }
        return entity;
    }

    @Override
    public void deleteByIds(Collection<ID> ids) {
        Assert.notNull(ids, "Ids must not be null");
        try {
            WhereBuilder where = WhereBuilder.b(
                    manager.getTable(actualClass).getId().getName(), "in", ids);
            manager.delete(actualClass, where);
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public long count() {
        try {
            return manager.selector(actualClass).count();
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }



}
