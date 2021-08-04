package com.example.bright_storage.service;

import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.service.base.AbstractCrudService;
import com.example.bright_storage.service.base.CrudService;

import java.util.Collection;
import java.util.List;

public interface StorageUnitService extends CrudService<StorageUnit, Long> {

    List<StorageUnit> listByCategoriesIn(Collection<Category> categories);

    /**
     * @see #listRecentActiveStorageUnit(String)
     * @return 最近活跃的存储单元
     */
    List<StorageUnit> listRecentActiveStorageUnit();

    /**
     * 根据{@link com.example.bright_storage.model.entity.AccessLog}
     * 查询最近最频繁访问的且名字中包含name的存储单元
     * @param name 用于查询的存储单元的名称
     * @return /
     */
    List<StorageUnit> listRecentActiveStorageUnit(String name);

    /**
     * @see #listLongestVisitedStorageUnits(Integer, Integer)
     * @return /
     */
    List<StorageUnit> listLongestVisitedStorageUnits();

    /**
     * 根据{@link StorageUnit#getLastAccessTime()}
     * 查询最久远访问的存储单元
     * @param storageUnitType 存储单元类型（0：物品，1：容器）
     * @param limit 查询数量
     * @return /
     */
    List<StorageUnit> listLongestVisitedStorageUnits(Integer storageUnitType, Integer limit);

}
