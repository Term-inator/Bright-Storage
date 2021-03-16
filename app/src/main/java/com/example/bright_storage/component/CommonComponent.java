package com.example.bright_storage.component;

import com.example.bright_storage.api.ErrorResponseHandler;
import com.example.bright_storage.service.impl.UserServiceImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {CommonModule.class})
public interface CommonComponent {

    void inject(ErrorResponseHandler<Object> errorResponseHandler);

}
