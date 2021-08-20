package com.example.bright_storage.service;

import com.example.bright_storage.model.dto.StorageUnitDTO;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.base.AbstractCrudService;
import com.example.bright_storage.service.base.CrudService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    /**
     * 查询关系共享的物品
     * @param relationId 关系id
     * @return /
     */
    List<StorageUnitDTO> listStorageUnitByRelationId(Long relationId);

    /**
     * 对关系共享物品，未同步（拥有服务器ID）的物品无法共享
     * @param relationId 关系ID
     * @param storageUnitId 物品服务器ID
     * @return ignore
     */
    BaseResponse<Object> shareStorageUnit(Long relationId, Long storageUnitId);

}
