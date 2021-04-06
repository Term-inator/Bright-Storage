package com.example.bright_storage.repository;

import android.database.sqlite.SQLiteDatabase;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.exception.DBException;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.entity.StorageUnitCategory;

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
        return entity;
    }
}
