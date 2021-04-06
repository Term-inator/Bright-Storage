package com.example.bright_storage.service.base;

import com.example.bright_storage.model.query.BaseQuery;
import com.example.bright_storage.model.support.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrudService<ENTITY, ID> {

    /**
     * 插入数据，并保存ID
     * @param entity /
     * @return 与参数entity相同
     */
    ENTITY create(ENTITY entity);

    /**
     * 插入多条数据
     * @param entities /
     * @return 与参数entities相同
     */
    List<ENTITY> create(Collection<ENTITY> entities);

    /**
     * 查询所有数据
     * @return /
     */
    List<ENTITY> listAll();

    /**
     * 查询ID属于集合ids中的数据
     * @param ids /
     * @return /
     */
    List<ENTITY> listByIdsIn(Collection<ID> ids);

    /**
     * 通过ID查询数据
     * @param id /
     * @return /
     */
    ENTITY getById(ID id);

    /**
     * 通过ID查询数据，如果找不到，抛出异常
     * @param id /
     * @return /
     */
    ENTITY getNotNullById(ID id);

    /**
     * where条件查询
     * @param query 条件查询
     * @return /
     */
    List<ENTITY> query(BaseQuery<ENTITY> query);

    /**
     * where条件查询+limit分页
     * @param query 条件查询
     * @param pageable 分页信息
     * @return /
     */
    List<ENTITY> query(BaseQuery<ENTITY> query, Pageable pageable);

    /**
     * 更新数据
     * @param entity /
     */
    void update(ENTITY entity);

    /**
     * 批量更新数据
     * @param entities /
     */
    void update(Collection<ENTITY> entities);

    /**
     * 删除数据
     * @param entity /
     */
    void delete(ENTITY entity);

    /**
     * 通过ID删除数据
     * @param id /
     * @return 被删除数据对应的实体对象
     */
    ENTITY deleteById(ID id);

    /**
     * 通过ID批量删除数据
     * @param ids /
     */
    void deleteByIdIn(Collection<ID> ids);

    /**
     * 查询数据条数
     * @return /
     */
    long count();
}
