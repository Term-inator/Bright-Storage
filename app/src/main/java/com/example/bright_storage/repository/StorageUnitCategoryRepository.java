package com.example.bright_storage.repository;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.exception.DBException;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnitCategory;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StorageUnitCategoryRepository
        extends AbstractRepository<StorageUnitCategory, Long>{

    public StorageUnitCategoryRepository(){
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        createTableIfNotExists();
    }

    public List<StorageUnitCategory> listByStorageUnitId(Long id){
        try {
            List<StorageUnitCategory> res = manager.selector(StorageUnitCategory.class)
                    .where("storage_unit_id","=",id)
                    .findAll();
            return res == null ? new ArrayList<>() : res;
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    public List<StorageUnitCategory> listByCategoryId(Long id){
        try {
            List<StorageUnitCategory> res = manager.selector(StorageUnitCategory.class)
                    .where("category_id","=",id)
                    .findAll();
            return res == null ? new ArrayList<>() : res;
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }

    public List<StorageUnitCategory> listByCategoriesIn(Collection<Category> categories){
        try {
            List<StorageUnitCategory> res = manager.selector(StorageUnitCategory.class)
                    .where("category_id","in",categories)
                    .findAll();
            return res == null ? new ArrayList<>() : res;
        } catch (DbException e) {
            throw new DBException(e.getMessage());
        }
    }
}
