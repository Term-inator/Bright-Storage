package com.example.bright_storage.component;

import com.example.bright_storage.model.entity.AccessLog;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.repository.AbstractRepository;
import com.example.bright_storage.repository.AccessLogRepository;
import com.example.bright_storage.repository.CategoryRepository;
import com.example.bright_storage.repository.StorageUnitCategoryRepository;
import com.example.bright_storage.repository.StorageUnitRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = RepositoryModule.class)
public interface RepositoryComponent {

    void inject(StorageUnitRepository storageUnitRepository);

    void inject(CategoryRepository categoryRepository);

    void inject(StorageUnitCategoryRepository storageUnitCategoryRepository);

    void inject(AccessLogRepository accessLogRepository);
}
