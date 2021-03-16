package com.example.bright_storage.component;

import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
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
    public StorageUnitService storageUnitService(){
        return new StorageUnitServiceImpl();
    }
}
