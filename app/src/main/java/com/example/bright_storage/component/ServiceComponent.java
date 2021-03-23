package com.example.bright_storage.component;

import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.example.bright_storage.service.impl.UserServiceImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class, RequestModule.class, CommonModule.class})
public interface ServiceComponent {

    void inject(UserServiceImpl userService);

    void inject(StorageUnitServiceImpl storageUnitService);

    void inject(CategoryServiceImpl categoryService);
}
