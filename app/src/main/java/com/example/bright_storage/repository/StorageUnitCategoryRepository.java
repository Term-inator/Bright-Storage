package com.example.bright_storage.repository;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.StorageUnitCategory;

public class StorageUnitCategoryRepository
        extends AbstractRepository<StorageUnitCategory, Long>{

    public StorageUnitCategoryRepository(){
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
    }
}
