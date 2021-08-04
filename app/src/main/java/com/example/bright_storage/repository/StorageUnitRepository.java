package com.example.bright_storage.repository;

import android.database.sqlite.SQLiteDatabase;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.exception.DBException;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.entity.StorageUnitCategory;

import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

public class StorageUnitRepository extends AbstractRepository<StorageUnit, Long>{

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    StorageUnitCategoryRepository storageUnitCategoryRepository;

    @Inject
    AccessLogRepository accessLogRepository;

    public StorageUnitRepository() {
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
    }

    public List<StorageUnit> listByCategoriesIn(Collection<Category> categories){
        List<StorageUnitCategory> suc =
                storageUnitCategoryRepository.listByCategoriesIn(categories);
        List<Long> ids = new ArrayList<>(suc.size());
        for (StorageUnitCategory re : suc) {
            ids.add(re.getStorageUnitId());
        }
        return findByIds(ids);
    }

    /**
     * 查询最久远访问的存储单元
     * @param storageUnitType 存储单元类型（0：物品，1：容器）
     * @param limit 查询数量
     * @return
     */
    public List<StorageUnit> listLongestVisitedStorageUnits(Integer storageUnitType, Integer limit){
        try {
            Selector<StorageUnit> selector = manager.selector(StorageUnit.class)
                    .orderBy("last_access_time", true);
            if(storageUnitType != null){
                selector.and("type", "=", storageUnitType);
            }
            if(limit != null && limit > 0){
                selector.limit(limit);
            }
            return selector.findAll();
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    protected StorageUnit postQuery(StorageUnit entity) {
        List<StorageUnitCategory> suc =
                storageUnitCategoryRepository.listByStorageUnitId(entity.getLocalId());
        List<Long> ids = new ArrayList<>(suc.size());
        for (StorageUnitCategory re : suc) {
            ids.add(re.getCategoryId());
        }
        List<Category> categories = categoryRepository.findByIds(ids);
        entity.setCategories(new HashSet<>(categories));

        return entity;
    }

    @Override
    protected StorageUnit postPersist(StorageUnit entity) {
        return postUpdate(entity);
    }

    @Override
    protected StorageUnit postUpdate(StorageUnit entity) {
        // 删除之前分类
        List<StorageUnitCategory> suc =
                storageUnitCategoryRepository.listByStorageUnitId(entity.getLocalId());
        storageUnitCategoryRepository.delete(suc);

        if(entity.getCategories() != null){
            List<StorageUnitCategory> categories = new ArrayList<>();
            for(Category c:entity.getCategories()){
                if(c.getLocalId() == null){
                    categoryRepository.save(c); // 保存未创建的分类
                }
                categories.add(new StorageUnitCategory(null, entity.getLocalId(), c.getLocalId()));
            }
            // 插入分类
            storageUnitCategoryRepository.save(categories);
        }

        return entity;
    }

    @Override
    protected StorageUnit postDelete(StorageUnit entity) {
        List<StorageUnitCategory> suc =
                storageUnitCategoryRepository.listByStorageUnitId(entity.getLocalId());
        storageUnitCategoryRepository.delete(suc);
        accessLogRepository.deleteByStorageUnitId(entity.getLocalId());
        return entity;
    }
}
