package com.example.bright_storage.repository;

import com.example.bright_storage.component.DaggerRepositoryComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.Category;

public class CategoryRepository extends AbstractRepository<Category, Long> {

    public CategoryRepository(){
        super();
        DaggerRepositoryComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
    }
}
