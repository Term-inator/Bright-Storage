package com.example.bright_storage.component;

import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.RelationService;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.SyncService;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.service.impl.RelationServiceImpl;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.example.bright_storage.service.impl.SyncServiceImpl;
import com.example.bright_storage.service.impl.UserServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Singleton
    @Provides
    public UserService providerUserService(){
        return new UserServiceImpl();
    }

    @Singleton
    @Provides
    public StorageUnitService providerStorageUnitService(){
        return new StorageUnitServiceImpl();
    }

    @Singleton
    @Provides
    public CategoryService providerCategoryService(){
        return new CategoryServiceImpl();
    }

    @Singleton
    @Provides
    public RelationService providerRelationService(){
        return new RelationServiceImpl();
    }

    @Singleton
    @Provides
    public SyncService providerSyncService(){
        return new SyncServiceImpl();
    }
}
