package com.example.bright_storage.repository;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnitCategory;

import java.util.List;

import javax.inject.Inject;

public class CategoryRepository extends AbstractRepository<Category, Long> {

    @Inject
    StorageUnitCategoryRepository storageUnitCategoryRepository;

    public CategoryRepository(){
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        createTableIfNotExists();
    }

    @Override
    protected Category postDelete(Category entity) {
        List<StorageUnitCategory> suc =
                storageUnitCategoryRepository.listByCategoryId(entity.getLocalId());
        storageUnitCategoryRepository.delete(suc);
        return entity;
    }
}
