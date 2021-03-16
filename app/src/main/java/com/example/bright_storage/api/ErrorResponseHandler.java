package com.example.bright_storage.api;


import com.example.bright_storage.component.CommonModule;
import com.example.bright_storage.component.DaggerCommonComponent;
import com.example.bright_storage.model.support.BaseResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import retrofit2.HttpException;

public class ErrorResponseHandler<T> implements Function<Throwable, T> {

    @Inject
    Gson gson;

    public ErrorResponseHandler(){

        DaggerCommonComponent.builder()
                .commonModule(new CommonModule())
                .build()
                .inject((ErrorResponseHandler<Object>) this);

    }

    @Override
    public T apply(Throwable throwable) {
        HttpException exception = (HttpException)throwable;
        try {
            String str = exception.response().errorBody().string();
            T response = gson.fromJson(str, (Type) BaseResponse.class);
            // do sth here
            System.out.println(response);
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
