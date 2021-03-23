package com.example.bright_storage.service.impl;

import com.example.bright_storage.component.DaggerServiceComponent;
import com.example.bright_storage.component.RepositoryModule;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.repository.CategoryRepository;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.base.AbstractCrudService;

import javax.inject.Inject;

public class CategoryServiceImpl extends AbstractCrudService<Category, Long> implements CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    public CategoryServiceImpl(){
        super();
        DaggerServiceComponent.builder()
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);
        super.repository = categoryRepository;
    }
}
